package woko2.hbcompass

import woko2.persistence.ResultIterator
import org.compass.core.CompassHits

class CompassResultIterator implements ResultIterator {

  private final CompassHits compassHits
  private final int start
  private final int limit
  private int position = 0

  CompassResultIterator(CompassHits compassHits, int start, int limit) {
    this.compassHits = compassHits
    this.start = start
    this.limit = limit
    this.position = this.start
  }

  Object next() {
    def res = compassHits.hit(position).getData()
    position++
    return res
  }

  boolean hasNext() {
    def end = limit <= 0 ? compassHits.length() : start + limit
    return position < end
  }

  int getStart() {
    return start
  }

  int getLimit() {
    return limit
  }

  int getTotalSize() {
    return compassHits.length()
  }


}
