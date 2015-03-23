package woko.pico;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.Disposable;
import org.picocontainer.PicoContainer;
import woko.ioc.AbstractWokoIocContainer;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.*;

public class WokoIocPico<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends AbstractWokoIocContainer<OsType,UmType,UnsType,FdmType> {

    private final PicoContainer pico;

    public WokoIocPico(PicoContainer picoContainer) {
        this.pico = picoContainer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getComponent(Object key) {
        return (T)pico.getComponent(key);
    }

    @Override
    public void close() {
        if (pico instanceof Disposable) {
            ((Disposable)pico).dispose();
        }
    }

    @Override
    public Map<?,?> getComponents() {
        Map<Object,Object> res = new HashMap<Object, Object>();
        Collection<ComponentAdapter<?>> componentAdapters = pico.getComponentAdapters();
        for (ComponentAdapter<?> a : componentAdapters) {
            Object key = a.getComponentKey();
            res.put(key, pico.getComponent(key));
        }
        return Collections.unmodifiableMap(res);
    }
}
