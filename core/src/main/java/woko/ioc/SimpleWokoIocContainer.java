package woko.ioc;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.Closeable;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple implementation of the IOC container for use cases where no heavy IOC is needed.
 */
public class SimpleWokoIocContainer<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends AbstractWokoIocContainer<OsType,UmType,UnsType,FdmType> {

    private final Map<Object,Object> components = new HashMap<Object, Object>();

    private static final WLogger logger = WLogger.getLogger(SimpleWokoIocContainer.class);

    /**
     * Create the simple IOC container with passed parameters
     * @param objectStore the <code>ObjectStore</code> to be used
     * @param userManager the <code>UserManager</code> to be used
     * @param usernameResolutionStrategy the <code>UsernameResolutionStrategy</code> to be used
     * @param facetDescriptorManager the <code>IFacetDescriptorManager</code> to be used
     */
    public SimpleWokoIocContainer(ObjectStore objectStore, UserManager userManager, UsernameResolutionStrategy usernameResolutionStrategy, IFacetDescriptorManager facetDescriptorManager) {
        this.addComponent(ObjectStore, objectStore)
            .addComponent(UserManager, userManager)
            .addComponent(UsernameResolutionStrategy, usernameResolutionStrategy)
            .addComponent(FacetDescriptorManager, facetDescriptorManager);
    }

    /**
     * Add a managed component to the IOC.
     * @param name the name (key) of the component
     * @param component the component
     * @return <code>this</code> for chained calls
     */
    public SimpleWokoIocContainer<OsType,UmType,UnsType,FdmType> addComponent(String name, Object component) {
        this.components.put(name, component);
        return this;
    }

    /**
     * Return the component for passed name (key)
     * @param name the name of the component
     * @param <T> the component type
     * @return the component for passed key
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getComponent(Object name) {
        return (T)this.components.get(name);
    }

    /**
     * Close the container : close all contained components that implement the
     * <code>Closeable</code> interface, and clear the components map.
     */
    @Override
    public void close() {
        logger.info("Closing components if needed...");
        for (Object cmp : components.values()) {
            if (cmp instanceof Closeable) {
                try {
                    logger.info("  * closing " + cmp + "...");
                    ((Closeable)cmp).close();
                    logger.info("  * ... closed " + cmp);
                } catch (Exception e) {
                    logger.error("Unable to close component " + cmp, e);
                }
            } else {
                logger.info("  * skipping " + cmp + " (does't implement Closeable)");
            }
        }
        components.clear();
    }

    @Override
    public Map<?,?> getComponents() {
        return Collections.unmodifiableMap(components);
    }
}
