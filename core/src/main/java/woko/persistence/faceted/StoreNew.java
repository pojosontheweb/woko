package woko.persistence.faceted;

import woko.persistence.ObjectStore;

public interface StoreNew {

  Object newInstance(ObjectStore store, Class<?> clazz);

}
