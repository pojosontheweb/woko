package woko.ioc;

import net.sourceforge.stripes.util.ReflectUtil;
import woko.Woko;
import woko.util.WLogger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

public class WokoInjectHelper {

    private static final WLogger logger = WLogger.getLogger(WokoInjectHelper.class);

    public static void injectComponents(Woko<?,?,?,?> woko, Object facet) {
        // iterate on public methods and find annotated
        // with @WokoInject
        Class<?> clazz = facet.getClass();
        for (Method m : ReflectUtil.getMethods(clazz)) {
            WokoInject wokoInject = m.getAnnotation(WokoInject.class);
            if (wokoInject!=null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Found method with @WokoInject : " + clazz.getName() + "#" + m.getName());
                }
                // annotation found, retrieve the service...
                String componentKey = wokoInject.value();
                Object component = woko.getIoc().getComponent(componentKey);
                if (component==null) {
                    logger.warn("Component " + componentKey + " not found in IoC ! Will not inject to " +
                            clazz.getName() + "#" + m.getName());
                } else {

                    // check if method has an appropriate signature
                    Class<?>[] paramTypes = m.getParameterTypes();
                    if (paramTypes.length!=1) {
                        logger.warn("Trying to use @WokoInject on a method that has an invalid signature. \n" +
                                "The method must accept a single parameter. \n" +
                                "Cannot inject to " + clazz.getName() + "#" + m.getName());
                    } else {

                        // component found, and method takes 1 arg, try to invoke
                        try {
                            m.invoke(facet, component);
                        } catch(Exception e) {
                            String msg = "Exception caught while trying to inject component " + component +
                                " for key " + componentKey + " into " + clazz.getName() + "#" + m.getName();
                            logger.error(msg);
                            throw new RuntimeException(msg, e);
                        }
                    }
                }
            }
        }

        Collection<Field> fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            WokoInject wokoInject = field.getAnnotation(WokoInject.class);
            if (wokoInject!=null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("@WokoInject found on field " + clazz.getName() + "#" + field.getName());
                }
                // field has @inject, try to find the setter
                PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(clazz, field.getName());
                boolean setterFound = false;
                if (pd != null) {
                    Method setter = pd.getWriteMethod();
                    if (setter!=null) {
                        setterFound = true;
                        String componentKey = wokoInject.value();
                        Object component = woko.getIoc().getComponent(componentKey);
                        if (component==null) {
                            logger.warn("Component " + componentKey + " not found in IoC ! Will not inject to " +
                                clazz.getName() + "#" + field.getName());
                        } else {
                            try {
                                setter.invoke(facet, component);
                            } catch (Exception e) {
                                String msg = "Exception caught while trying to inject component " + component +
                                    " for key " + componentKey + " into field " + clazz.getName() + "#" + field.getName() +
                                    " using setter.";
                                logger.error(msg);
                                throw new RuntimeException(msg, e);
                            }
                        }
                    }
                }
                if (!setterFound) {
                    String msg = "Field " + clazz.getName() + "#" + field.getName() + " + has @WokoInject but no property descriptor found (No public setter).";
                    logger.error(msg);
                    throw new RuntimeException(msg);
                }
            }
        }


    }

}
