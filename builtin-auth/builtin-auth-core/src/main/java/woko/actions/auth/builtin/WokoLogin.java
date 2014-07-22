package woko.actions.auth.builtin;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.rpc.RpcInterceptor;
import net.sourceforge.stripes.rpc.RpcResolutionWrapper;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import org.json.JSONException;
import org.json.JSONObject;
import woko.Woko;
import woko.actions.BaseActionBean;
import woko.actions.WokoActionBean;
import woko.actions.WokoActionBeanContext;
import woko.facets.builtin.auth.PostLoginFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.JsonResolution;
import woko.util.WLogger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Stripes <code>ActionBean</code> that manages built-in authentication.
*/
@UrlBinding("/login")
@StrictBinding
public class WokoLogin<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseActionBean<OsType,UmType,UnsType,FdmType> {

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

    /**
     * Default handler : displays the login form.
     * Handles switch to HTTPS if needed.
     */
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


    /**
     * <code>login</code> handler : attempts to authenticate using the configured
     * {@link UserManager}.
     *
     * If authentication succeeds, sets the <code>username</code> in the HTTP session,
     * invokes <code>postLogin</code> facet and redirects to the <code>targetUrl</code>.
     *
     * If authentication fails, tosses error messages in the <code>WokoActionBeanContext</code>
     * and shows login FORM again.
     *
     * @see UserManager#authenticate(String, javax.servlet.http.HttpServletRequest)
     */
    public Resolution login() {
        log.debug("trying to log-in user " + username);
        String user = authenticate();
        if (user != null) {
            // auth OK, invoke PostLogin facet if any
            WokoActionBeanContext<OsType,UmType,UnsType,FdmType> context = getContext();
            // bind username to http session
            context.getRequest().getSession().setAttribute(SESSION_ATTR_CURRENT_USER, user);
            Woko<OsType,UmType,UnsType,FdmType> woko = context.getWoko();
            PostLoginFacet pl = woko.getFacet(PostLoginFacet.FACET_NAME, context.getRequest(), null, Object.class);
            if (pl!=null) {
                pl.execute(user);
            }
            // add message and redirect to the target URL
            context.getMessages().add(new LocalizableMessage(KEY_MSG_LOGIN_SUCCESS));
            log.debug(username + " logged in, redirecting to " + targetUrl);
            return new RpcResolutionWrapper(new RedirectResolution(targetUrl)) {
                @Override
                public Resolution getRpcResolution() {
                    JSONObject result = new JSONObject();
                    try {
                        result.put("username", username);
                    } catch(JSONException e) {
                        throw new RuntimeException(e);
                    }
                    return new JsonResolution(result);
                }
            };
        } else {
            // authentication failed, add messages to context, and redirect to login
            log.warn("Authentication failed for user '" + username + "', redirecting to login form again");
            WokoActionBeanContext<OsType,UmType,UnsType,FdmType> abc = getContext();
            abc.getValidationErrors().addGlobalError(new LocalizableError(KEY_MSG_LOGIN_FAILED));
            if (!RpcInterceptor.isRpcRequest(abc.getRequest())) {
                abc.getResponse().setStatus(401);
            }
            return new RpcResolutionWrapper(displayForm()) {
                @Override
                public Resolution getRpcResolution() {
                    JSONObject result = new JSONObject();
                    try {
                        result.put("success", false);
                    } catch(JSONException e) {
                        throw new RuntimeException(e);
                    }
                    return new JsonResolution(result);
                }
            };
        }
    }

}
