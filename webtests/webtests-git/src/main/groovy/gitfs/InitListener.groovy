package gitfs

import woko.WokoInitListener
import woko.Woko
import woko.inmemory.InMemoryUserManager
import com.rvkb.gitfs.store.GitObjectStore
import com.rvkb.gitfs.GitFS
import com.rvkb.gitfs.GitFSBuilder
import woko.auth.builtin.SessionUsernameResolutionStrategy
import woko.persistence.ObjectStore
import woko.users.UserManager
import woko.users.UsernameResolutionStrategy

class InitListener extends WokoInitListener {

  @Override protected ObjectStore createObjectStore() {
    new GitObjectStore(new File("/tmp/myrepo/.git"), ['gitfs'])
  }

  @Override protected UserManager createUserManager() {
    InMemoryUserManager um = new InMemoryUserManager()
    um.addUser("wdevel", "wdevel", ["developer"])
    return um
  }

  @Override protected UsernameResolutionStrategy createUsernameResolutionStrategy() {
    new SessionUsernameResolutionStrategy()
  }

}
