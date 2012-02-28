package woko.actions.auth.builtin;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import woko.actions.BaseActionBean;
import woko.actions.WokoActionBeanContext;
import woko.util.WLogger;

@UrlBinding("/login")
public class WokoLogin extends BaseActionBean {

  private static final WLogger log = WLogger.getLogger(WokoLogin.class);
  private static final String KEY_MSG_LOGIN_FAILED  = "woko.login.failed";
  private static final String KEY_MSG_LOGIN_SUCCESS  = "woko.login.success";

  public static final String SESSION_ATTR_CURRENT_USER = "__CURRENT_USER";

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

  @DefaultHandler
  @DontValidate
  public Resolution displayForm() {
      log.debug("Displaying login form");
      return new ForwardResolution("/WEB-INF/woko/jsp/login.jsp");
  }

  public Resolution login() {
      log.debug("trying to log-in user " + username);
      String user = authenticate();
      if (user!=null) {
          // auth OK, add message and redirect to the target URL
          WokoActionBeanContext context = getContext();
          context.getMessages().add(new LocalizableMessage(KEY_MSG_LOGIN_SUCCESS));
          context.getRequest().getSession().setAttribute(SESSION_ATTR_CURRENT_USER, user);
          log.debug(username + " logged in, redirecting to " + targetUrl);
          return new RedirectResolution(targetUrl);
      } else {
          // authentication failed, add messages to context, and redirect to login
          log.warn("Authentication failed for user '" + username + "', redirecting to login form again");
          getContext().getValidationErrors().addGlobalError(new LocalizableError(KEY_MSG_LOGIN_FAILED));
          return displayForm();
      }
  }

}
