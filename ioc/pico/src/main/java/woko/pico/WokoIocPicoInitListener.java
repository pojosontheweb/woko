package woko.pico;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import woko.WokoIocInitListener;
import woko.ioc.WokoIocContainer;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

public abstract class WokoIocPicoInitListener<OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends WokoIocInitListener<OsType,UmType,UnsType,FdmType> {

    protected MutablePicoContainer createPicoAndInitFdm() {
        return new DefaultPicoContainer()
            .addComponent(
                WokoIocContainer.FacetDescriptorManager,
                createAnnotatedFdm()
            );
    }

    @Override
    protected WokoIocContainer<OsType,UmType,UnsType,FdmType> createIocContainer() {
        MutablePicoContainer pico = createPicoAndInitFdm();
        addObjectStore(pico);
        addUserManager(pico);
        addUsernameResolutionStrategy(pico);
        pico.start();
        return new WokoIocPico<OsType,UmType,UnsType,FdmType>(pico);
    }

    protected abstract void addUsernameResolutionStrategy(MutablePicoContainer pico);

    protected abstract void addUserManager(MutablePicoContainer pico);

    protected abstract void addObjectStore(MutablePicoContainer pico);

}
