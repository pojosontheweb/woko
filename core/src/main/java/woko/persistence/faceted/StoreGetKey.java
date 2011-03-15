package woko.persistence.faceted;

import woko.persistence.ObjectStore;

public interface StoreGetKey {

  String getKey(ObjectStore store, Object object);

}
