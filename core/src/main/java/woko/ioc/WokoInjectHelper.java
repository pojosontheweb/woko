package woko.ioc;

import net.sourceforge.stripes.util.ReflectUtil;
import woko.Woko;
import woko.util.WLogger;

import java.lang.reflect.Method;

public class WokoInjectHelper {

    private static final WLogger logger = WLogger.getLogger(WokoInjectHelper.class);

    public static void injectComponents(Woko<?,?,?,?> woko, Object facet) {
        // iterate on public methods and find annotated
        // with @WokoInject
        for (Method m : ReflectUtil.getMethods(facet.getClass())) {
            WokoInject wokoInject = m.getAnnotation(WokoInject.class);
            if (wokoInject!=null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Found method with @WokoInject : " + facet.getClass().getName() + "#" + m.getName());
                }
                // annotation found, retrieve the service...
                String componentKey = wokoInject.value();
                Object component = woko.getIoc().getComponent(componentKey);
                if (component==null) {
                    logger.warn("Component " + componentKey + " not found in IoC ! Will not inject to " +
                            facet.getClass().getName() + "#" + m.getName());
                } else {

                    // check if method has an appropriate signature
                    Class<?>[] paramTypes = m.getParameterTypes();
                    if (paramTypes.length!=1) {
                        logger.warn("Trying to use @WokoInject on a method that has an invalid signature. \n" +
                                "The method must accept a single parameter. \n" +
                                "Cannot inject to " + facet.getClass().getName() + "#" + m.getName());
                    } else {

                        // component found, and method takes 1 arg, try to invoke
                        try {
                            m.invoke(facet, component);
                        } catch(Exception e) {
                            logger.error("Exception caught while trying to inject component " + component +
                                " for key " + componentKey + " into " + facet.getClass().getName() + "#" + m.getName());
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

}
