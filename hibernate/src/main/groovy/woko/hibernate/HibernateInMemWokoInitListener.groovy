package woko.hibernate

import woko.users.UserManager
import woko.inmemory.InMemoryUserManager

class HibernateInMemWokoInitListener extends HibernateWokoInitListener {

  protected UserManager createUserManager() {
    InMemoryUserManager um = new InMemoryUserManager()
    um.addUser('wdevel', 'wdevel', ['developer'])
    return um
  }

}
