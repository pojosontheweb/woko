package woko.ioc;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.Map;

/**
 * Interface for the Woko IOC container. The <code>Woko</code> instance references an IOC container
 * for accessing the various mandatory components (<code>ObjectStore</code>, <code>UserManager</code> etc.).
 * The IOC container can also be used for optional, application-specific components.
 */
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

    /**
     * Return the component for passed <code>key</code> if any (null if no such component)
     * @param key the component key
     * @param <T> the component type
     * @return the component for passed key
     */
    <T> T getComponent(Object key);

    /**
     * Return the <code>ObjectStore</code> for the app
     * @return the <code>ObjectStore</code> for the app
     */
    OsType getObjectStore();

    /**
     * Return the <code>UserManager</code> for the app
     * @return the <code>UserManager</code> for the app
     */
    UmType getUserManager();

    /**
     * Return the <code>UsernameResolutionStrategy</code> for the app
     * @return the <code>UsernameResolutionStrategy</code> for the app
     */
    UnsType getUsernameResolutionStrategy();

    /**
     * Return the <code>IFacetDescriptorManager</code> for the app
     * @return the <code>IFacetDescriptorManager</code> for the app
     */
    FdmType getFacetDescriptorManager();

    /**
     * Return a map of all keys/components in the ioc. This map
     * should be immutable, and should never be modified. It is
     * meant to be used read-only.
     * @return a map of all keys/components
     */
    Map<?,?> getComponents();

}
