package test

import woko.WokoIocInitListener
import woko.actions.auth.rememberme.RmCookieStore
import woko.actions.auth.rememberme.hibernate.HibernateRmCookieStore
import woko.auth.builtin.SessionUsernameResolutionStrategy
import woko.ext.blobs.BlobStore
import woko.ext.blobs.hibernate.HibernateBlobStore
import woko.hbcompass.HibernateCompassStore
import woko.inmemory.InMemoryUserManager
import woko.users.RemoteUserStrategy
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
import woko.ioc.WokoIocContainer
import woko.ioc.SimpleWokoIocContainer
import woko.hibernate.HibernateStore

class BootstrapWebtestsInitListener extends
        WokoIocInitListener{

    @Override
    protected WokoIocContainer createIocContainer() {
        HibernateStore store = new HibernateCompassStore(getPackageNamesFromConfig(HibernateStore.CTX_PARAM_PACKAGE_NAMES, true));

        return new SimpleWokoIocContainer(
                store,
                new InMemoryUserManager().addUser("wdevel", "wdevel", ["blobmanager", "developer"]),
                new SessionUsernameResolutionStrategy(),
                createAnnotatedFdm()
        ).addComponent(BlobStore.KEY, new HibernateBlobStore(store))
        .addComponent(RmCookieStore.KEY, new HibernateRmCookieStore(store))
    }


}
