package woko.actions.auth.builtin;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import woko.Woko;
import woko.actions.BaseActionBean;
import woko.actions.WokoActionBeanContext;
import woko.facets.builtin.auth.PostLoginFacet;
import woko.util.WLogger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@UrlBinding("/login")
public class WokoLogin extends BaseActionBean {

    private static final WLogger log = WLogger.getLogger(WokoLogin.class);
    private static final String KEY_MSG_LOGIN_FAILED = "woko.login.failed";
    private static final String KEY_MSG_LOGIN_SUCCESS = "woko.login.success";

    public static final String SESSION_ATTR_CURRENT_USER = "__CURRENT_USER";

    public static final String CTX_INIT_PARAM_WOKO_SSL_ENABLED = "Woko.Ssl.Enabled";
    public static final String CTX_INIT_PARAM_WOKO_SSL_SERVER_NAME = "Woko.Ssl.Server.Name";
    public static final String CTX_INIT_PARAM_WOKO_SSL_SERVER_PORT = "Woko.Ssl.Server.Port";

    @Validate(required = true)
    private String username;

    private String targetUrl = "/home";

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

    protected String authenticate() {
        if (getContext().getWoko().getUserManager().authenticate(username, getContext().getRequest())) {
            return username;
        }
        return null;
    }

    private Resolution forward() {
        return new ForwardResolution("/WEB-INF/woko/jsp/login.jsp");
    }

    @DefaultHandler
    @DontValidate
    public Resolution displayForm() {
        // check if SSL is enabled
        ServletContext ctx = getContext().getServletContext();
        String sslEnabledParam = ctx.getInitParameter(CTX_INIT_PARAM_WOKO_SSL_ENABLED);
        boolean sslEnabled = sslEnabledParam!=null && !sslEnabledParam.toLowerCase().equals("false");
        if (!sslEnabled) {
            return forward();
        } else {
            HttpServletRequest request = getContext().getRequest();
            if (request.isSecure()) {
                return forward();
            } else {
                String serverName = ctx.getInitParameter(CTX_INIT_PARAM_WOKO_SSL_SERVER_NAME);
                if (serverName==null) {
                    serverName = request.getServerName();
                }
                String serverPortHttpsStr = ctx.getInitParameter(CTX_INIT_PARAM_WOKO_SSL_SERVER_PORT);
                int port = 443;
                if (serverPortHttpsStr!=null) {
                    try {
                       port = Integer.parseInt(serverPortHttpsStr);
                    } catch(NumberFormatException e) {
                        // default to 443
                        log.warn("Could not parse HTTPs port");
                    }
                }
                log.debug("Request is not secure, redirecting to HTTPs with serverName '" + serverName +
                        "' and port " + port);
                return new LoginHttpsRedirectResolution(serverName, port);
            }
        }
    }



    public Resolution login() {
        log.debug("trying to log-in user " + username);
        String user = authenticate();
        if (user != null) {
            // auth OK, invoke PostLogin facet if any
            WokoActionBeanContext context = getContext();
            // bind username to http session
            context.getRequest().getSession().setAttribute(SESSION_ATTR_CURRENT_USER, user);
            Woko woko = context.getWoko();
            PostLoginFacet pl = (PostLoginFacet)woko.getFacet(PostLoginFacet.FACET_NAME, context.getRequest(), null, Object.class);
            if (pl!=null) {
                pl.execute(user);
            }
            // add message and redirect to the target URL
            context.getMessages().add(new LocalizableMessage(KEY_MSG_LOGIN_SUCCESS));
            log.debug(username + " logged in, redirecting to " + targetUrl);
            return new RedirectResolution(targetUrl);
        } else {
            // authentication failed, add messages to context, and redirect to login
            log.warn("Authentication failed for user '" + username + "', redirecting to login form again");
            getContext().getValidationErrors().addGlobalError(new LocalizableError(KEY_MSG_LOGIN_FAILED));
            getContext().getResponse().setStatus(401);
            return displayForm();
        }
    }

}
