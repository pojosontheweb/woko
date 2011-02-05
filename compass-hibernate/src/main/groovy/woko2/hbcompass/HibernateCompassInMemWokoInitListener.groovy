package woko2.hbcompass

import woko2.inmemory.InMemoryUserManager
import woko2.users.UserManager

class HibernateCompassInMemWokoInitListener extends HibernateCompassWokoInitListener {

  protected UserManager createUserManager() {
    InMemoryUserManager um = new InMemoryUserManager()
    um.addUser('wdevel', 'wdevel', ['developer'])
    return um
  }

}
