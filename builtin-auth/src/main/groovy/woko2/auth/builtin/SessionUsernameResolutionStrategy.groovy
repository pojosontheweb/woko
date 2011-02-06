package woko2.auth.builtin

import javax.servlet.http.HttpServletRequest
import woko2.users.UsernameResolutionStrategy
import woko2.actions.auth.builtin.WokoLogin

class SessionUsernameResolutionStrategy implements UsernameResolutionStrategy {

  String getUsername(HttpServletRequest request) {
    return (String)request.session.getAttribute(WokoLogin.SESSION_ATTR_CURRENT_USER)
  }

}
