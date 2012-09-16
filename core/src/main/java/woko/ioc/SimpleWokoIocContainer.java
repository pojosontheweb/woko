package woko.ioc;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.HashMap;
import java.util.Map;

public class SimpleWokoIocContainer<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends AbstractWokoIocContainer<OsType,UmType,UnsType,FdmType> {

    private final Map<Object,Object> components = new HashMap<Object, Object>();

    public SimpleWokoIocContainer(ObjectStore objectStore, UserManager userManager, UsernameResolutionStrategy usernameResolutionStrategy, IFacetDescriptorManager facetDescriptorManager) {
        addComponent(ObjectStore, objectStore);
        addComponent(UserManager, userManager);
        addComponent(UsernameResolutionStrategy, usernameResolutionStrategy);
        addComponent(FacetDescriptorManager, facetDescriptorManager);
    }

    public void addComponent(String name, Object component) {
        this.components.put(name, component);
    }

    @Override
    public <T> T getComponent(Object name) {
        @SuppressWarnings("unchecked")
        T o = (T)this.components.get(name);
        return o;
    }

    @Override
    public void close() {
        components.clear();
    }

}
