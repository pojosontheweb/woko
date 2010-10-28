package woko2.mongo

import woko2.users.UserManager
import woko2.users.User

class MongoUserManager implements UserManager {

  private final WokoMongo wokoMongo

  def MongoUserManager(WokoMongo wokoMongo) {
    this.wokoMongo = wokoMongo;
  }

  def User getUser(String username) {
    return wokoMongo.db.users.findOne(username:username)
  }

  def List<String> getRoles(String username) {
    User u = getUser(username)
    if (u==null) {
      return Collections.emptyList()
    }
    return u.roles
  }

  def boolean checkPassword(String username, String clearPassword) {
    User u = getUser(username)
    if (u==null) {
      return false
    }
    return u.password = clearPassword
  }


}
