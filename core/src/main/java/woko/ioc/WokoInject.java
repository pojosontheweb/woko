package woko.ioc;

import java.lang.annotation.*;

/**
 * Annotation that can be placed on facets, in order to auto-inject components
 * from the IoC.
 * Can be used on public "setter methods", or private fields with a setter.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface WokoInject {

    /**
     * Key of the component to be injected. If null, auto-wiring (by type) will be used.
     * @return the key of the component in the ioc container, or null
     */
    String value() default "";

}
