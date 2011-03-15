package gitfs

import woko.WokoInitListener
import woko.Woko
import woko.inmemory.InMemoryUserManager
import com.rvkb.gitfs.store.GitObjectStore
import com.rvkb.gitfs.GitFS
import com.rvkb.gitfs.GitFSBuilder
import woko.auth.builtin.SessionUsernameResolutionStrategy

class InitListener extends WokoInitListener {

  @Override
  protected Woko createWoko() {
    InMemoryUserManager um = new InMemoryUserManager();
    um.addUser("wdevel", "wdevel", Arrays.asList("developer"));
    return new Woko(
        new GitObjectStore(new File("/tmp/myrepo/.git"), ['gitfs']),
        um,
        Arrays.asList(Woko.ROLE_GUEST),
        Woko.createDefaultFacetDescriptorManager(),
        new SessionUsernameResolutionStrategy());
  }

}
