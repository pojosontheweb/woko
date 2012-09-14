package woko.ioc;

import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.HashMap;
import java.util.Map;

public class BackCompatContainer<OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy> implements WokoIocContainer {

    private final OsType objectStore;
    private final UmType userManager;
    private final UnsType usernameResolutionStrategy;

    private final Map<Class<?>,Object> components = new HashMap<Class<?>, Object>();

    public BackCompatContainer(OsType objectStore, UmType userManager, UnsType usernameResolutionStrategy) {
        this.objectStore = objectStore;
        this.userManager = userManager;
        this.usernameResolutionStrategy = usernameResolutionStrategy;
    }

    public void addComponents(Object... components) {
        for (Object o : components) {
            this.components.put(o.getClass(), o);
        }
    }

    @Override
    public <T> T getComponent(Class<T> componentClass) {
        @SuppressWarnings("unchecked")
        T o = (T)this.components.get(componentClass);
        return o;
    }

}
