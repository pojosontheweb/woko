package test;

import woko.WokoIocInitListener;
import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.hbcompass.HibernateCompassStore;
import woko.ioc.SimpleWokoIocContainer;
import woko.ioc.WokoIocContainer;
import woko.mail.ConsoleMailService;
import woko.mail.MailService;
import woko.push.PushFacetDescriptorManager;

public class UserManagementWebtestsInitListener
        extends WokoIocInitListener<HibernateCompassStore, MyUserManager, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> {

    @Override
    protected WokoIocContainer<HibernateCompassStore, MyUserManager, SessionUsernameResolutionStrategy, PushFacetDescriptorManager> createIocContainer() {
        HibernateCompassStore store = new HibernateCompassStore(getPackageNamesFromConfig(HibernateCompassStore.CTX_PARAM_PACKAGE_NAMES, true));
        return new SimpleWokoIocContainer<HibernateCompassStore, MyUserManager, SessionUsernameResolutionStrategy, PushFacetDescriptorManager>(
                store,
                new MyUserManager(store).createDefaultUsers(),
                new SessionUsernameResolutionStrategy(),
                new PushFacetDescriptorManager(createAnnotatedFdm())
        ).addComponent(MailService.KEY, new ConsoleMailService());
    }
}
