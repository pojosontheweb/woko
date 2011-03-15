package woko.persistence.faceted;

import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;

public interface StoreList {

  ResultIterator list(ObjectStore store, String className, Integer start, Integer limit);

}
