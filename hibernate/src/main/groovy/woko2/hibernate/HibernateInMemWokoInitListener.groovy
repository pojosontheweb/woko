package woko2.hibernate

import woko2.users.UserManager
import woko2.inmemory.InMemoryUserManager

class HibernateInMemWokoInitListener extends HibernateWokoInitListener {

  protected UserManager createUserManager() {
    InMemoryUserManager um = new InMemoryUserManager()
    um.addUser('wdevel', 'wdevel', ['developer'])
    return um
  }

}
