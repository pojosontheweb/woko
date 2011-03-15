package woko.facets.builtin.gitfs

import net.sourceforge.jfacets.annotations.FacetKey
import woko.persistence.faceted.StoreLoad
import woko.persistence.ObjectStore
import com.rvkb.gitfs.GitFS
import com.rvkb.gitfs.Session
import org.codehaus.jackson.map.ObjectMapper

@FacetKey(name="storeLoad", profileId="developer")
class GitStoreLoad extends GitStoreFacetBase implements StoreLoad {

  Object load(ObjectStore store, Class<?> clazz, String key) {
    GitFS gfs = store.gfs
    return gfs.doInSession(userInfo) { Session session ->
      return session.readFile(new File(session.getAbsolutePath(key))) { InputStream is ->
        ObjectMapper om = new ObjectMapper();
        return om.readValue(is, clazz)
      }
    }
  }


}
