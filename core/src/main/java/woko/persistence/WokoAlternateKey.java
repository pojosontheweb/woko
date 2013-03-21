package woko.persistence;

import java.lang.annotation.*;

/**
 * Used to generate and use alternate keys in Woko URLs and ObjectLoader.
 * Annotate a Managed POJO's field or get/setter in order to use this property
 * as the alternate key for instances of the POJO class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface WokoAlternateKey {

    String altKeyProperty();

}
