package woko.pico;

import junit.framework.TestCase;
import net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import woko.Woko;
import woko.inmemory.InMemoryUserManager;
import woko.ioc.WokoIocContainer;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.users.RemoteUserStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WokoIocPicoTest extends TestCase {

    @Test
    public void testWokoInitWithPico() {
        List<String> packagesNames = Arrays.asList("foo", "bar");
        List<String> pkgs = new ArrayList<String>();
        if (packagesNames != null && packagesNames.size() > 0) {
            pkgs.addAll(packagesNames);
        }
        pkgs.addAll(Woko.DEFAULT_FACET_PACKAGES);
        MutablePicoContainer pico = new DefaultPicoContainer()
                .addComponent(
                        WokoIocContainer.FacetDescriptorManager,
                        new AnnotatedFacetDescriptorManager(pkgs).initialize()
                )
                .addComponent(
                        WokoIocContainer.ObjectStore,
                        MyStore.class
                )
                .addComponent(
                        WokoIocContainer.UserManager,
                        new InMemoryUserManager().addUser("wdevel", "wdevel", Arrays.asList("developer"))
                )
                .addComponent(
                        WokoIocContainer.UsernameResolutionStrategy,
                        RemoteUserStrategy.class
                );
        pico.start();
        WokoIocPico wokoPico = new WokoIocPico(pico);

        doCheckClass(wokoPico, WokoIocContainer.ObjectStore, MyStore.class);
        doCheckClass(wokoPico, WokoIocContainer.FacetDescriptorManager, AnnotatedFacetDescriptorManager.class);
        doCheckClass(wokoPico, WokoIocContainer.UserManager, InMemoryUserManager.class);
        doCheckClass(wokoPico, WokoIocContainer.UsernameResolutionStrategy, RemoteUserStrategy.class);
    }

    private void doCheckClass(WokoIocPico ioc, Object componentName, Class<?> expectedClass) {
        Object component = ioc.getComponent(componentName);
        assertNotNull("component is null for name " + componentName);
        assertEquals("unexpected class for component with name " + componentName, expectedClass, component.getClass());
    }

    public static class MyStore implements ObjectStore {

        @Override
        public Object load(String className, String key) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object save(Object obj) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object delete(Object obj) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getKey(Object obj) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public String getClassMapping(Class<?> clazz) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Class<?> getMappedClass(String className) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ResultIterator<?> list(String className, Integer start, Integer limit) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public List<Class<?>> getMappedClasses() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public ResultIterator<?> search(Object query, Integer start, Integer limit) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void close() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

}