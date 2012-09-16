package woko.ioc;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

public abstract class AbstractWokoIocContainer<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > implements WokoIocContainer<OsType,UmType,UnsType,FdmType> {

    @Override
    public OsType getObjectStore() {
        return getComponent(ObjectStore);
    }

    @Override
    public UmType getUserManager() {
        return getComponent(UserManager);
    }

    @Override
    public UnsType getUsernameResolutionStrategy() {
        return getComponent(UsernameResolutionStrategy);
    }

    @Override
    public FdmType getFacetDescriptorManager() {
        return getComponent(FacetDescriptorManager);
    }

    @Override
    public void close() {
    }
}
