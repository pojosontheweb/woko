package woko.facets.builtin.gitfs

import woko.persistence.faceted.StoreNew
import net.sourceforge.jfacets.annotations.FacetKey
import woko.persistence.ObjectStore

@FacetKey(name="storeNew", profileId="developer")
class GitStoreNew extends GitStoreFacetBase implements StoreNew {

  Object newInstance(ObjectStore store, Class<?> clazz) {
    return clazz.newInstance()
  }


}
