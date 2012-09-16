package test

import woko.WokoIocInitListener
import woko.auth.builtin.SessionUsernameResolutionStrategy
import woko.inmemory.InMemoryUserManager
import woko.hbcompass.HibernateCompassStore
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import woko.ioc.WokoIocContainer
import woko.ioc.SimpleWokoIocContainer
import woko.hibernate.HibernateStore

class LocalizationWebtestsInitListener extends
        WokoIocInitListener<HibernateCompassStore,InMemoryUserManager,SessionUsernameResolutionStrategy, AnnotatedFacetDescriptorManager> {

    @Override
    protected WokoIocContainer<HibernateCompassStore,InMemoryUserManager,SessionUsernameResolutionStrategy, AnnotatedFacetDescriptorManager> createIocContainer() {
        return new SimpleWokoIocContainer<HibernateCompassStore,InMemoryUserManager,SessionUsernameResolutionStrategy, AnnotatedFacetDescriptorManager>(
                new HibernateCompassStore(getPackageNamesFromConfig(HibernateStore.CTX_PARAM_PACKAGE_NAMES, true)),
                new InMemoryUserManager().addUser("wdevel", "wdevel", ["developer"]),
                new SessionUsernameResolutionStrategy(),
                createAnnotatedFdm());
    }


}
