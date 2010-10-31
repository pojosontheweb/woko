package net.sourceforge.stripes.auth;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.util.Log;

import javax.servlet.http.HttpServletRequest;

public abstract class LoginActionBean<U> implements ActionBean, AuthConstants {

    private static final Log log = Log.getInstance(LoginActionBean.class);
    
    public static final String KEY_MSG_LOGIN_FAILED  = "stripes.login.failed";
    public static final String KEY_MSG_LOGIN_SUCCESS  = "stripes.login.success";

    private ActionBeanContext context;

    @Validate(required = true)
    private String username;

    @Validate(required = true)
    private String password;

    private String targetUrl;

    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
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
            return displayForm();
        }
    }

    protected abstract U authenticate();
    
}
