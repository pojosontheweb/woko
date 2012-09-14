package woko.pico;

import org.picocontainer.Disposable;
import org.picocontainer.PicoContainer;
import woko.ioc.WokoIocContainer;

public class WokoIocPico implements WokoIocContainer {

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
}
