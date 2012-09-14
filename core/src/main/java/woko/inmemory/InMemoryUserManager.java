/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  public InMemoryUserManager addUser(String username, String password, List<String> roles) {
    if (users.containsKey(username)) {
      throw new IllegalArgumentException("User " + username + " already exists");
    }
    InMemoryUser imu = new InMemoryUser();
    imu.setUsername(username);
    imu.setPassword(password);
    imu.setRoles(roles);
    users.put(username, imu);
    return this;
  }

}
