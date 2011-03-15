package woko.persistence.faceted;

import woko.persistence.ObjectStore;

public interface StoreSave {

  Object save(ObjectStore store, Object obj);

}
