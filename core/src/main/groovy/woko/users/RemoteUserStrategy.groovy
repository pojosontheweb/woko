package woko.users

import javax.servlet.http.HttpServletRequest

class RemoteUserStrategy implements UsernameResolutionStrategy {

  String getUsername(HttpServletRequest request) {
    return request.remoteUser
  }


}
