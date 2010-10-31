package net.sourceforge.stripes.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

/**
 * Annotation that allows to mark action beans or their event methods for authentication
 * control.
 * If placed on the bean class, it acts as the default for all event handlers.
 * Can also be placed on an even handler method, in case your don't want all your
 * event handler to use authentication.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequiresAuthentication {
}
