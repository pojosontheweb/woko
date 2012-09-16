package woko.ioc;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

public interface WokoIocContainer<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > {

    static final String ObjectStore = "ObjectStore";
    static final String UserManager = "UserManager";
    static final String FacetDescriptorManager = "FacetDescriptorManager";
    static final String UsernameResolutionStrategy = "UsernameResolutionStrategy";

    Object getComponent(Object key);

    OsType getObjectStore();

    UmType getUserManager();

    UnsType getUsernameResolutionStrategy();

    FdmType getFacetDescriptorManager();

    void close();

}
