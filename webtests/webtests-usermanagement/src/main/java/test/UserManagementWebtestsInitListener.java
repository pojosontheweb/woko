package test;

import woko.WokoIocInitListener;
import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.ext.usermanagement.hibernate.HibernateUserManager;
import woko.hbcompass.HibernateCompassStore;
import woko.ioc.SimpleWokoIocContainer;
import woko.ioc.WokoIocContainer;
import woko.mail.ConsoleMailService;
import woko.mail.MailService;
import woko.push.PushFacetDescriptorManager;
import woko.users.UserManager;

import java.util.Arrays;
import java.util.List;

public class UserManagementWebtestsInitListener
        extends WokoIocInitListener<HibernateCompassStore, HibernateUserManager<?,MyUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> {

    @Override
    protected WokoIocContainer<HibernateCompassStore, HibernateUserManager<?,MyUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> createIocContainer() {
        HibernateCompassStore store = new HibernateCompassStore(getPackageNamesFromConfig(HibernateCompassStore.CTX_PARAM_PACKAGE_NAMES, true));
        HibernateUserManager<?,MyUser> userManager = new HibernateUserManager(store, MyUser.class)
                .setRegisteredRoles(Arrays.asList("developer"))
                .createDefaultUsers();
        return new SimpleWokoIocContainer<HibernateCompassStore, HibernateUserManager<?,MyUser>, SessionUsernameResolutionStrategy, PushFacetDescriptorManager>(
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
