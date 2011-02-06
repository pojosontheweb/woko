package woko.inmemory

import woko.WokoInitListener
import woko.Woko

class InMemoryWokoInitListener extends WokoInitListener {

  Woko createWoko() {
    return doCreateWoko()
  }

  public static Woko doCreateWoko() {
    def um = new InMemoryUserManager()
    um.addUser 'wdevel', 'wdevel', ['developer']
    return new Woko(new InMemoryObjectStore(), um, [Woko.ROLE_GUEST], Woko.createDefaultFacetDescriptorManager(), Woko.createDefaultUsernameResolutionStrategy())
  }

}
