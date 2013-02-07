package woko.ioc;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.Closeable;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

/**
 * Base class for <code>Woko</code> IOC containers. Provides implementation of mandatory components
 * accessor methods.
 */
public abstract class AbstractWokoIocContainer<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > implements WokoIocContainer<OsType,UmType,UnsType,FdmType>, Closeable {

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

}
