package woko.auth.builtin;

import woko.actions.auth.builtin.WokoLogin;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;

/**
 * Strategy that Retrieves username in HTTP session.
 *
 * @see WokoLogin
 */
public class SessionUsernameResolutionStrategy implements UsernameResolutionStrategy {

  public String getUsername(HttpServletRequest request) {
    return (String)request.getSession().getAttribute(WokoLogin.SESSION_ATTR_CURRENT_USER);
  }

}
