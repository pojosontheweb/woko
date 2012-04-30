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

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.util.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class LoginActionBean<U, C extends ActionBeanContext> implements ActionBean, AuthConstants {

    private static final Log log = Log.getInstance(LoginActionBean.class);
    
    public static final String KEY_MSG_LOGIN_FAILED  = "woko.login.failed";
    public static final String KEY_MSG_LOGIN_SUCCESS  = "woko.login.success";

    private C context;

    @Validate(required = true)
    private String username;

    @Validate(required = true)
    private String password;

    private String targetUrl;

    public C getContext() {
        return context;
    }

    @SuppressWarnings("unchecked")
    public void setContext(ActionBeanContext context) {
        this.context = (C)context;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @DefaultHandler
    @DontValidate
    public Resolution displayForm() {
        log.debug("Displaying login form");
        return new ForwardResolution("/WEB-INF/auth/login.jsp");
    }

    public Resolution login() {
        log.debug("trying to log-in user ", username);
        U user = authenticate();
        if (user!=null) {
            // auth OK, add message and redirect to the target URL
            getContext().getMessages().add(new LocalizableMessage(KEY_MSG_LOGIN_SUCCESS));
            HttpServletRequest request = context.getRequest();
            request.getSession().setAttribute(SESSION_ATTR_CURRENT_USER, user);
            if (targetUrl==null) {
                targetUrl = DEFAULT_TARGET_URL;
            }
            log.debug(username, " logged in, redirecting to ", targetUrl);
            return new RedirectResolution(targetUrl);
        } else {
            // authentication failed, add messages to context, and redirect to login
            log.warn("Authentication failed for user '", username, "', redirecting to login form again");
            getContext().getValidationErrors().addGlobalError(new LocalizableError(KEY_MSG_LOGIN_FAILED));
            getContext().getResponse().setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return displayForm();
        }
    }

    protected abstract U authenticate();
    
}
