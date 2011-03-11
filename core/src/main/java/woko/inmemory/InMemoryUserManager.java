package woko.inmemory;

import woko.users.UserManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserManager implements UserManager {

  Map<String,InMemoryUser> users = new HashMap<String,InMemoryUser>();

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

  public boolean checkPassword(String username, String password) {
    InMemoryUser u = getUser(username);
    if (u==null) {
      return false;
    }
    String pwd = u.getPassword();
    return pwd != null && pwd.equals(password);
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
