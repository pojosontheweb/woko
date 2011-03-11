package woko.users;

import javax.servlet.http.HttpServletRequest;

public class RemoteUserStrategy implements UsernameResolutionStrategy {

  public String getUsername(HttpServletRequest request) {
    return request.getRemoteUser();
  }

}
