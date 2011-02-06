package woko.hbcompass

import woko.inmemory.InMemoryUserManager
import woko.users.UserManager

class HibernateCompassInMemWokoInitListener extends HibernateCompassWokoInitListener {

  protected UserManager createUserManager() {
    InMemoryUserManager um = new InMemoryUserManager()
    um.addUser('wdevel', 'wdevel', ['developer'])
    return um
  }

}
