package woko2.inmemory

import woko2.Woko
import woko2.users.UserManager
import woko2.persistence.ObjectStore

class InMemoryWoko extends Woko {  

  def InMemoryWoko(List<String> fallbackRoles) {
    super(new InMemoryObjectStore(), new InMemoryUserManager(), fallbackRoles)
  }

  protected void customInit() {
    userManager.addUser 'wdevel', 'wdevel', ['developer']
  }


}
