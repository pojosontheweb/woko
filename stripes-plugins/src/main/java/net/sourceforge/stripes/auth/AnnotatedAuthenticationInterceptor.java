/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.stripes.auth;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.util.Log;

import java.lang.reflect.Method;

public class AnnotatedAuthenticationInterceptor extends AuthenticationInterceptor {

    private static final Log log = Log.getInstance(AnnotatedAuthenticationInterceptor.class);

    /**
     * Uses @RequiresAuthentication annotation in order to check for authentication
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
                    return true;
                }
            }
        }
        log.debug("no annotations found, authentication isn't required");
        return false;
    }
    
}
