package test

import woko.WokoIocInitListener
import woko.ext.blobs.BlobStore
import woko.ext.blobs.hibernate.HibernateBlobStore
import woko.ext.categories.CategoryManager
import woko.ext.categories.hibernate.HibernateCategoryManager
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
    protected WokoIocContainer<HibernateCompassStore,InMemoryUserManager,RemoteUserStrategy,AnnotatedFacetDescriptorManager> createIocContainer() {
        HibernateStore store = new HibernateCompassStore(getPackageNamesFromConfig(HibernateStore.CTX_PARAM_PACKAGE_NAMES, true));

        return new SimpleWokoIocContainer<HibernateCompassStore,InMemoryUserManager,RemoteUserStrategy,AnnotatedFacetDescriptorManager>(
                store,
                new InMemoryUserManager().addUser("wdevel", "wdevel", ["developer", "categorymanager"]),
                new RemoteUserStrategy(),
                createAnnotatedFdm()
        )
                .addComponent(BlobStore.KEY, new HibernateBlobStore(store))
                .addComponent(CategoryManager.KEY, new HibernateCategoryManager(store))
    }


}
