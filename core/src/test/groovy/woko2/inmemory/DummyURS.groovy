package woko2.inmemory

import woko2.users.UsernameResolutionStrategy
import javax.servlet.http.HttpServletRequest

class DummyURS implements UsernameResolutionStrategy {

  String username

  def String getUsername(HttpServletRequest request) {
    return username
  }


}
