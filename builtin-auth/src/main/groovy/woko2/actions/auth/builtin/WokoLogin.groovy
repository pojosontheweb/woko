package woko2.actions.auth.builtin

import net.sourceforge.stripes.action.UrlBinding

import net.sourceforge.stripes.validation.Validate
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.DontValidate
import net.sourceforge.stripes.action.DefaultHandler
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.action.LocalizableMessage

import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.validation.LocalizableError
import woko2.util.WLogger
import woko2.actions.BaseActionBean

@UrlBinding("/login")
class WokoLogin extends BaseActionBean {

  private static final WLogger log = WLogger.getLogger(WokoLogin.class)

  static final String KEY_MSG_LOGIN_FAILED  = "stripes.login.failed"
  static final String KEY_MSG_LOGIN_SUCCESS  = "stripes.login.success"
  static final String SESSION_ATTR_CURRENT_USER = "__CURRENT_USER"

  @Validate(required = true)
  String username

  @Validate(required = true)
  String password

  String targetUrl = "/home"

  protected String authenticate() {
    if (context.woko.userManager.checkPassword(username, password)) {
      return username
    }
    return null
  }

  @DefaultHandler
  @DontValidate
  public Resolution displayForm() {
      log.debug("Displaying login form");
      return new ForwardResolution("/WEB-INF/woko/jsp/login.jsp")
  }

  public Resolution login() {
      log.debug("trying to log-in user $username")
      String user = authenticate()
      if (user!=null) {
          // auth OK, add message and redirect to the target URL
          context.messages.add(new LocalizableMessage(KEY_MSG_LOGIN_SUCCESS))
          context.request.session.setAttribute(SESSION_ATTR_CURRENT_USER, user)
          log.debug("$username logged in, redirecting to $targetUrl")
          return new RedirectResolution(targetUrl)
      } else {
          // authentication failed, add messages to context, and redirect to login
          log.warn("Authentication failed for user '$username', redirecting to login form again")
          context.validationErrors.addGlobalError(new LocalizableError(KEY_MSG_LOGIN_FAILED))
          return displayForm()
      }
  }

}
