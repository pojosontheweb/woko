package woko.hbcompass;

import woko.inmemory.InMemoryUserManager;
import woko.users.UserManager;

import java.util.Arrays;

public class HibernateCompassInMemWokoInitListener extends HibernateCompassWokoInitListener {

  protected UserManager createUserManager() {
    InMemoryUserManager um = new InMemoryUserManager();
    um.addUser("wdevel", "wdevel", Arrays.asList("developer"));
    return um;
  }

}
