package woko.ioc;

import java.lang.annotation.*;

/**
 * Annotation that can be placed on facet methods in order to auto-inject components
 * from the IoC into facets.
 * Must be used on public methods that accept only a parameter (the component required).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface WokoInject {

    String value();

}
