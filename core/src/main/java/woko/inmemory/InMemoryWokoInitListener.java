package woko.inmemory;

import woko.Woko;
import woko.WokoInitListener;

import java.util.Arrays;

public class InMemoryWokoInitListener extends WokoInitListener {

  protected Woko createWoko() {
    return doCreateWoko();
  }

  public static Woko doCreateWoko() {
    InMemoryUserManager um = new InMemoryUserManager();
    um.addUser("wdevel", "wdevel", Arrays.asList("developer"));
    return new Woko(
        new InMemoryObjectStore(),
        um,
        Arrays.asList(Woko.ROLE_GUEST),
        Woko.createDefaultFacetDescriptorManager(),
        Woko.createDefaultUsernameResolutionStrategy());
  }

}
