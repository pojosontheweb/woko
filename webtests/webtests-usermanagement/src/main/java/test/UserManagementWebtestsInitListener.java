package test;

import woko.WokoIocInitListener;
import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.ext.usermanagement.core.AccountStatus;
import woko.ext.usermanagement.hibernate.HibernateUserManager;
import woko.ext.usermanagement.mail.BindingHelper;
import woko.hbcompass.HibernateCompassStore;
import woko.ioc.SimpleWokoIocContainer;
import woko.ioc.WokoIocContainer;
import woko.mail.ConsoleMailService;
import woko.mail.MailService;
import woko.mail.SmtpMailService;
import woko.persistence.TransactionCallback;
import woko.push.PushFacetDescriptorManager;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class UserManagementWebtestsInitListener
        extends WokoIocInitListener<HibernateCompassStore, HibernateUserManager<MyUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> {

    @Override
    protected WokoIocContainer<HibernateCompassStore, HibernateUserManager<MyUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> createIocContainer() {
        final HibernateCompassStore store = new HibernateCompassStore(getPackageNamesFromConfig(HibernateCompassStore.CTX_PARAM_PACKAGE_NAMES, true));
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
        store.doInTransaction(new TransactionCallback() {
            @Override
            public void execute() throws Exception {
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
            store.doInTransaction(new TransactionCallback() {
                @Override
                public void execute() throws Exception {
                    store.save(u1);
                }
            });
        }

        MailService mailService;
        final String username = System.getProperty("woko.webtests.smtp.username");
        if (username!=null) {
            mailService = new SmtpMailService("http://localhost:8080/woko-webtests",
                    "yikes@pojosontheweb.com",
                    BindingHelper.createDefaultMailTemplates()) {
                @Override
                protected Properties getMailSessionProperties() {
                    Properties props = super.getMailSessionProperties();
                    props.put("mail.smtp.username", username);
                    props.put("mail.smtp.password", System.getProperty("woko.webtests.smtp.password"));
                    return props;
                }
            };
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
