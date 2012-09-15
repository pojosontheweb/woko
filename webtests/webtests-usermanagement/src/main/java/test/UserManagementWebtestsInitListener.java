package test;

import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager;
import woko.WokoIocInitListener;
import woko.auth.builtin.SessionUsernameResolutionStrategy;
import woko.hbcompass.HibernateCompassStore;
import woko.ioc.SimpleWokoIocContainer;
import woko.ioc.WokoIocContainer;

public class UserManagementWebtestsInitListener
        extends WokoIocInitListener<HibernateCompassStore,MyUserManager,SessionUsernameResolutionStrategy,AnnotatedFacetDescriptorManager> {

    @Override
    protected WokoIocContainer createIocContainer() {
        HibernateCompassStore store = new HibernateCompassStore(getPackageNamesFromConfig(HibernateCompassStore.CTX_PARAM_PACKAGE_NAMES, true));
        return new SimpleWokoIocContainer(
                store,
                new MyUserManager(store).createDefaultUsers(),
                new SessionUsernameResolutionStrategy(),
                new AnnotatedFacetDescriptorManager(getFacetPackages())
        );
    }
}
