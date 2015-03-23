package woko.ioc;

import net.sourceforge.stripes.util.ReflectUtil;
import woko.Woko;
import woko.util.WLogger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public class WokoInjectHelper {

    private static final WLogger logger = WLogger.getLogger(WokoInjectHelper.class);

    private static Object getComponent(Class<?> componentType, WokoInject wokoInject, Woko<?,?,?,?> woko) {
        String componentKey = wokoInject.value();
        WokoIocContainer<?,?,?,?> ioc = woko.getIoc();
        if (componentKey.equals("")) {
            // no key specified, auto-wire !
            Map<?,?> keysAndComponents = ioc.getComponents();
            Collection<?> components = keysAndComponents.values();
            Object res = null;
            // make sure there ain't several components matching...
            for (Object c : components) {
                if (componentType.isAssignableFrom(c.getClass())) {
                    if (res==null) {
                        res = c;
                    } else {
                        throw new IllegalStateException("More than 1 component matching component type "
                            + componentType + " ! auto-wiring error.");
                    }
                }
            }
            return res;
        } else {
            // key specified, return the component from IoC
            return ioc.getComponent(componentKey);
        }
    }

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
                // check if method has an appropriate signature
                Class<?>[] paramTypes = m.getParameterTypes();
                if (paramTypes.length!=1) {
                    logger.warn("Trying to use @WokoInject on a method that has an invalid signature. \n" +
                        "The method must accept a single parameter. \n" +
                        "Cannot inject to " + clazz.getName() + "#" + m.getName());
                } else {
                    Class<?> argType = paramTypes[0];

                    // annotation found, retrieve the service...
                    Object component = getComponent(argType, wokoInject, woko);
                    if (component == null) {
                        logger.warn("Component not found in IoC ! Will not inject to method " +
                            clazz.getName() + "#" + m.getName());
                    } else {

                        // component found, and method takes 1 arg, try to invoke
                        try {
                            m.invoke(facet, component);
                        } catch (Exception e) {
                            String msg = "Exception caught while trying to inject component " + component +
                                " for key into " + clazz.getName() + "#" + m.getName();
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
                        Object component = getComponent(field.getType(), wokoInject, woko);
                        if (component==null) {
                            logger.warn("Component not found in IoC ! Will not inject to " +
                                clazz.getName() + "#" + field.getName());
                        } else {
                            try {
                                setter.invoke(facet, component);
                            } catch (Exception e) {
                                String msg = "Exception caught while trying to inject component " + component +
                                    " into field " + clazz.getName() + "#" + field.getName() +
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
