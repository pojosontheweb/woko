package woko.persistence.faceted;

import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;

public interface StoreSearch {

  ResultIterator search(ObjectStore store, Object query, Integer start, Integer limit);

}
