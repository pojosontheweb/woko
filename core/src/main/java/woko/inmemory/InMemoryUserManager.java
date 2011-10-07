package woko.inmemory;

import woko.users.UserManager;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserManager implements UserManager {

  private Map<String,InMemoryUser> users = new HashMap<String,InMemoryUser>();

  private InMemoryUser getUser(String username) {
    return users.get(username);
  }

  public List<String> getRoles(String username) {
    InMemoryUser u = getUser(username);
    if (u==null) {
      return Collections.emptyList();
    }
    return u.getRoles();
  }

  protected String extractPassword(HttpServletRequest request) {
    return request.getParameter("password");
  }

  public boolean authenticate(String username, HttpServletRequest request) {
    InMemoryUser u = getUser(username);
    if (u==null) {
      return false;
    }
    String pwd = u.getPassword();
    return pwd != null && pwd.equals(extractPassword(request));
  }

  public void addUser(String username, String password, List<String> roles) {
    if (users.containsKey(username)) {
      throw new IllegalArgumentException("User " + username + " already exists");
    }
    InMemoryUser imu = new InMemoryUser();
    imu.setUsername(username);
    imu.setPassword(password);
    imu.setRoles(roles);
    users.put(username, imu);
  }

}
