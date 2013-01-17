package woko.ioc;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.Closeable;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import java.util.HashMap;
import java.util.Map;

public class SimpleWokoIocContainer<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends AbstractWokoIocContainer<OsType,UmType,UnsType,FdmType> {

    private final Map<Object,Object> components = new HashMap<Object, Object>();

    private static final WLogger logger = WLogger.getLogger(SimpleWokoIocContainer.class);

    public SimpleWokoIocContainer(ObjectStore objectStore, UserManager userManager, UsernameResolutionStrategy usernameResolutionStrategy, IFacetDescriptorManager facetDescriptorManager) {
        this.addComponent(ObjectStore, objectStore)
            .addComponent(UserManager, userManager)
            .addComponent(UsernameResolutionStrategy, usernameResolutionStrategy)
            .addComponent(FacetDescriptorManager, facetDescriptorManager);
    }

    public SimpleWokoIocContainer<OsType,UmType,UnsType,FdmType> addComponent(String name, Object component) {
        this.components.put(name, component);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getComponent(Object name) {
        return (T)this.components.get(name);
    }

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

}
