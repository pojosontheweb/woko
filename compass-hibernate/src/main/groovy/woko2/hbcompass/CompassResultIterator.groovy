package woko2.hbcompass

import woko2.persistence.ResultIterator
import org.compass.core.CompassHitsOperations
import org.compass.core.CompassHit

class CompassResultIterator implements ResultIterator {

  private final CompassHitsOperations compassHits
  private final int start
  private final int limit
  private final int totalSize
  private Iterator<CompassHit> iterator

  CompassResultIterator(CompassHitsOperations compassHits, int start, int limit, int totalSize) {
    compassHits.iterator()
    this.compassHits = compassHits
    this.start = start
    this.limit = limit
    this.totalSize = totalSize
    this.iterator = compassHits.iterator()
  }

  Object next() {
    return iterator.next().getData()
  }

  boolean hasNext() {
    iterator.hasNext()
  }

  int getStart() {
    return start
  }

  int getLimit() {
    return limit
  }

  int getTotalSize() {
    return totalSize
  }


}
