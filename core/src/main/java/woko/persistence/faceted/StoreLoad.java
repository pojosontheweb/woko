package woko.persistence.faceted;

import woko.persistence.ObjectStore;

public interface StoreLoad {

  Object load(ObjectStore store, Class<?> clazz, String key);

}
