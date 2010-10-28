package woko2.users

import javax.servlet.http.HttpServletRequest

public interface UsernameResolutionStrategy {

  String getUsername(HttpServletRequest request)

}