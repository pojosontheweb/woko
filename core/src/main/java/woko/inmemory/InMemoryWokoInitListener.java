package woko.inmemory;

import woko.Woko;
import woko.WokoInitListener;
import woko.persistence.ObjectStore;
import woko.users.UserManager;

import java.util.Arrays;

public class InMemoryWokoInitListener extends WokoInitListener {

  @Override
  protected ObjectStore createObjectStore() {
    return new InMemoryObjectStore();
  }

  @Override
  protected UserManager createUserManager() {
    InMemoryUserManager um = new InMemoryUserManager();
    um.addUser("wdevel", "wdevel", Arrays.asList("developer"));
    return um;
  }

}
