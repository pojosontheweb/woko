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
    @SuppressWarnings("unchecked")
    public OsType getObjectStore() {
        return (OsType)getComponent(ObjectStore);
    }

    @Override
    @SuppressWarnings("unchecked")
    public UmType getUserManager() {
        return (UmType)getComponent(UserManager);
    }

    @Override
    @SuppressWarnings("unchecked")
    public UnsType getUsernameResolutionStrategy() {
        return (UnsType)getComponent(UsernameResolutionStrategy);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FdmType getFacetDescriptorManager() {
        return (FdmType)getComponent(FacetDescriptorManager);
    }

    @Override
    public void close() {
    }
}
