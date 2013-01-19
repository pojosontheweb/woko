package test;

import org.hibernate.Session;
import woko.WokoIocInitListener;
import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.ext.usermanagement.core.AccountStatus;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.hibernate.HibernateUserManager;
import woko.ext.usermanagement.mail.BindingHelper;
import woko.hbcompass.HibernateCompassStore;
import woko.hibernate.HibernateStore;
import woko.hibernate.TxCallback;
import woko.ioc.SimpleWokoIocContainer;
import woko.ioc.WokoIocContainer;
import woko.mail.ConsoleMailService;
import woko.mail.MailService;
import woko.mail.SmtpMailService;
import woko.push.PushFacetDescriptorManager;
import woko.users.UserManager;

import java.util.Arrays;
import java.util.List;

public class UserManagementWebtestsInitListener
        extends WokoIocInitListener<HibernateCompassStore, HibernateUserManager<MyUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> {

    @Override
    protected WokoIocContainer<HibernateCompassStore, HibernateUserManager<MyUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> createIocContainer() {
        HibernateCompassStore store = new HibernateCompassStore(getPackageNamesFromConfig(HibernateCompassStore.CTX_PARAM_PACKAGE_NAMES, true));
        HibernateUserManager<MyUser> userManager = new HibernateUserManager<MyUser>(store, MyUser.class)
                .setRegisteredRoles(Arrays.asList("developer"));

        final MyUser wdevel = new MyUser();
        wdevel.setUsername("wdevel");
        wdevel.setPassword(userManager.encodePassword("wdevel"));
        wdevel.setAccountStatus(AccountStatus.Active);
        wdevel.setEmail("wdevel@woko.com");
        wdevel.setRoles(Arrays.asList("usermanager", "developer")); // user manager has to be first to avoid developer's list facet to take precedence
        wdevel.setAccountStatus(AccountStatus.Active);
        wdevel.setProp1("not null in there"); // need to set not null prop
        store.doInTx(new TxCallback() {
            @Override
            public void execute(HibernateStore store, Session session) throws Exception {
                store.save(wdevel);
            }
        });

        for (int i=0; i<1000; i++) {
            final MyUser u1 = new MyUser();
            u1.setUsername("testuser" + i);
            u1.setPassword(userManager.encodePassword("testuser" + i));
            u1.setAccountStatus(AccountStatus.Active);
            u1.setEmail("testemail" + i + "@foo.bar");
            u1.setRoles(Arrays.asList("testuser"));
            u1.setProp1("foobar" + i);
            store.doInTx(new TxCallback() {
                @Override
                public void execute(HibernateStore store, Session session) throws Exception {
                    store.save(u1);
                }
            });
        }

        MailService mailService;
        String smtpHost = System.getProperty("woko.webtests.smtp.host");
        if (smtpHost!=null) {
            mailService = new SmtpMailService("http://www.pojosontheweb.com",
                    "yikes@pojosontheweb.com",
                    BindingHelper.createDefaultMailTemplates(),
                    smtpHost,
                    Integer.parseInt(System.getProperty("woko.webtests.smtp.port")),
                    true)
                    .setSmtpAuthUsername(System.getProperty("woko.webtests.smtp.username"))
                    .setSmtpAuthPassword(System.getProperty("woko.webtests.smtp.password"));
        }  else {
            mailService = new ConsoleMailService(
                    "http://www.pojosontheweb.com",
                    "yikes@pojosontheweb.com",
                    BindingHelper.createDefaultMailTemplates());
        }

        return new SimpleWokoIocContainer<HibernateCompassStore, HibernateUserManager<MyUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager>(
                store,
                userManager,
                new SessionUsernameResolutionStrategy(),
                new PushFacetDescriptorManager(createAnnotatedFdm())
        ).addComponent(MailService.KEY, mailService);
    }

    @Override
    protected List<String> createFallbackRoles() {
        return Arrays.asList("myguest");
    }
}
