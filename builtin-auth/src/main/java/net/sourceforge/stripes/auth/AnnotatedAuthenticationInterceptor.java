package net.sourceforge.stripes.auth;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.util.Log;

import java.lang.reflect.Method;

public class AnnotatedAuthenticationInterceptor extends AuthenticationInterceptor {

    /** Log instance used to log information from this class. */
    private static final Log log = Log.getInstance(AnnotatedAuthenticationInterceptor.class);

    /**
     * Uses @ResuiresAuthentication annotation in order to check for authentication
     * @param executionContext the execution context
     * @return true if passed authentication requires authentication
     */
    protected boolean requiresAuthentication(ExecutionContext executionContext) {
        ActionBean ab = executionContext.getActionBean();
        if (ab!=null) {
            Class<?> beanClass = ab.getClass();
            if (beanClass.isAnnotationPresent(RequiresAuthentication.class)) {
                // bean class annotated, all handlers require authentication
                log.debug("class ", beanClass.getName(), " is annotated, authentication required");
                return true;
            } else {
                Method handler = executionContext.getHandler();
                if (handler!=null && handler.isAnnotationPresent(RequiresAuthentication.class)) {
                    log.debug("handler ", handler.getName(), " is annotated, authentication required");
                    return handler.isAnnotationPresent(RequiresAuthentication.class);
                }
            }
        }
        log.debug("no annotations found, authentication isn't required");
        return false;
    }
    
}
