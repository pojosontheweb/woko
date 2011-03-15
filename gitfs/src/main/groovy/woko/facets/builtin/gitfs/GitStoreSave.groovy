package woko.facets.builtin.gitfs

import woko.persistence.faceted.StoreSave
import net.sourceforge.jfacets.annotations.FacetKey
import woko.persistence.ObjectStore
import com.rvkb.gitfs.Session
import com.rvkb.gitfs.GitFS
import org.codehaus.jackson.map.ObjectMapper

@FacetKey(name="storeSave", profileId="developer")
class GitStoreSave extends GitStoreFacetBase implements StoreSave {

  Object save(ObjectStore store, Object obj) {
    GitFS gfs = store.gfs
    String key = store.getKey(obj)
    if (key==null) {
      return obj
    }
    return gfs.doInSession(userInfo) { Session session ->
      return session.writeToFile(new File(session.getAbsolutePath(key)), "commit message") { OutputStream os ->
        ObjectMapper om = new ObjectMapper()
        om.writeValue(os, obj)
        return obj
      }
    }
  }


}
