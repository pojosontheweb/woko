package woko.auth.builtin

import javax.servlet.http.HttpServletRequest
import woko.users.UsernameResolutionStrategy
import woko.actions.auth.builtin.WokoLogin

class SessionUsernameResolutionStrategy implements UsernameResolutionStrategy {

  String getUsername(HttpServletRequest request) {
    return (String)request.session.getAttribute(WokoLogin.SESSION_ATTR_CURRENT_USER)
  }

}
