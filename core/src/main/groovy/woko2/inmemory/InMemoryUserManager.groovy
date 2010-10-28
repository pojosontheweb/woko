package woko2.inmemory

import woko2.users.UserManager
import woko2.users.User

class InMemoryUserManager implements UserManager {

  def users = new HashMap<String,InMemoryUser>()

  def User getUser(String username) {
    return users[username]
  }

  def List<String> getRoles(String username) {
    def u = getUser(username)
    if (u==null) {
      return Collections.emptyList()
    }
    return u.roles
  }

  def boolean checkPassword(String username, String clearPassword) {
    def u = getUser(username)
    return u?.password == clearPassword
  }

  void addUser(String username, String password, List<String> roles) {
    if (users.containsKey(username)) {
      throw new IllegalArgumentException("User $username amready exists")
    }
    users[username] = new InMemoryUser(username:username, password:password, roles:roles)
  }

}
