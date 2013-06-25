package test

import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import woko.WokoIocInitListener
import woko.ext.blobs.BlobStore
import woko.ext.blobs.hibernate.HibernateBlobStore
import woko.hbcompass.HibernateCompassStore
import woko.hibernate.HibernateStore
import woko.inmemory.InMemoryUserManager
import woko.ioc.SimpleWokoIocContainer
import woko.ioc.WokoIocContainer
import woko.users.RemoteUserStrategy

class PurecssInitListener extends
        WokoIocInitListener<HibernateCompassStore, InMemoryUserManager, RemoteUserStrategy, AnnotatedFacetDescriptorManager>{

    @Override
    protected WokoIocContainer<HibernateCompassStore, InMemoryUserManager, RemoteUserStrategy, AnnotatedFacetDescriptorManager> createIocContainer() {
        HibernateStore store = new HibernateCompassStore(getPackageNamesFromConfig(HibernateStore.CTX_PARAM_PACKAGE_NAMES, true));

        return new SimpleWokoIocContainer<HibernateCompassStore, InMemoryUserManager, RemoteUserStrategy, AnnotatedFacetDescriptorManager>(
                store,
                new InMemoryUserManager().addUser("wdevel", "wdevel", ["blobmanager", "developer"]),
                new RemoteUserStrategy(),
                createAnnotatedFdm()
        ).addComponent(BlobStore.KEY, new HibernateBlobStore(store))
    }


}
