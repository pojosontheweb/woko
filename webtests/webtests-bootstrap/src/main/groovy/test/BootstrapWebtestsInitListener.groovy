package test

import woko.WokoIocInitListener
import woko.hbcompass.HibernateCompassStore
import woko.inmemory.InMemoryUserManager
import woko.users.RemoteUserStrategy
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import woko.ioc.WokoIocContainer
import woko.ioc.SimpleWokoIocContainer
import woko.hibernate.HibernateStore

class BootstrapWebtestsInitListener extends
        WokoIocInitListener<HibernateCompassStore,InMemoryUserManager,RemoteUserStrategy,AnnotatedFacetDescriptorManager>{

    @Override
    protected WokoIocContainer createIocContainer() {
        return new SimpleWokoIocContainer(
                new HibernateCompassStore(getPackageNamesFromConfig(HibernateStore.CTX_PARAM_PACKAGE_NAMES, true)),
                new InMemoryUserManager().addUser("wdevel", "wdevel", ["developer"]),
                new RemoteUserStrategy(),
                createAnnotatedFdm());
    }


}
