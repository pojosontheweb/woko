package woko.persistence.faceted;

import woko.persistence.ObjectStore;

public interface StoreDelete {

  Object delete(ObjectStore store, Object object);

}
