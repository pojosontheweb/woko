package woko.users

import javax.servlet.http.HttpServletRequest

public interface UsernameResolutionStrategy {

  String getUsername(HttpServletRequest request)

}