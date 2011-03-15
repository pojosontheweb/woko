package woko.facets.builtin.gitfs

import woko.persistence.faceted.StoreGetKey
import woko.persistence.ObjectStore
import com.rvkb.gitfs.store.GitEntity
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="storeGetKey", profileId="developer")
class GitStoreGetKey extends GitStoreFacetBase implements StoreGetKey {

  String getKey(ObjectStore store, Object object) {
    GitEntity ge = object.getClass().getAnnotation(GitEntity.class)
    if (ge==null) {
      return null
    }
    String idPropName = ge.keyProp()
    try {
      return object[idPropName]
    } catch(MissingPropertyException e) {
      throw new IllegalStateException("keyProp $idPropName supplied in @GitEntity class ${object.getClass()} is not a valid property", e)
    }
  }


}
