package woko.hbcompass;

import org.compass.core.CompassHit;
import org.compass.core.CompassHitsOperations;
import woko.persistence.ResultIterator;

import java.util.Iterator;

public class CompassResultIterator<T> extends ResultIterator<T> {

  private final int start;
  private final int limit;
  private final int totalSize;
  private Iterator<CompassHit> iterator;

  public CompassResultIterator(CompassHitsOperations compassHits, int start, int limit, int totalSize) {
    this.start = start;
    this.limit = limit;
    this.totalSize = totalSize;
    this.iterator = compassHits.iterator();
  }

  public T next() {
    @SuppressWarnings("unchecked")
    T next = (T)iterator.next().getData();
    return next;
  }

  public boolean hasNext() {
    return iterator.hasNext();
  }

  public int getStart() {
    return start;
  }

  public int getLimit() {
    return limit;
  }

  public int getTotalSize() {
    return totalSize;
  }


}
