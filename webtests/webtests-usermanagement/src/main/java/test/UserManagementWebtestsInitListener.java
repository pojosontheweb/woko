package test;

import org.hibernate.Session;
import woko.WokoIocInitListener;
import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.ext.usermanagement.core.AccountStatus;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.hibernate.HibernateUserManager;
import woko.hbcompass.HibernateCompassStore;
import woko.hibernate.HibernateStore;
import woko.hibernate.TxCallback;
import woko.ioc.SimpleWokoIocContainer;
import woko.ioc.WokoIocContainer;
import woko.mail.ConsoleMailService;
import woko.mail.MailService;
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
                .setDefaultRoles(Arrays.asList("usermanager", "developer")) // usermanager has to come first otherwise /list/Myuser fallbacks to developer's list
                .createDefaultUsers()
                .setRegisteredRoles(Arrays.asList("developer"));

        for (int i=0; i<1000; i++) {
            final MyUser u1 =  userManager.createUser("testuser" + i, "testuser" + i, "testemail" + i + "@foo.bar",  Arrays.asList("testuser"), AccountStatus.Active);
            u1.setProp1("foobar" + i);
            store.doInTx(new TxCallback() {
                @Override
                public void execute(HibernateStore store, Session session) throws Exception {
                    store.save(u1);
                }
            });
        }

        return new SimpleWokoIocContainer<HibernateCompassStore, HibernateUserManager<MyUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager>(
                store,
                userManager,
                new SessionUsernameResolutionStrategy(),
                new PushFacetDescriptorManager(createAnnotatedFdm())
        ).addComponent(MailService.KEY, new ConsoleMailService("http://www.pojosontheweb.com", "yikes@pojosontheweb.com"));
    }

    @Override
    protected List<String> createFallbackRoles() {
        return Arrays.asList("myguest");
    }
}
