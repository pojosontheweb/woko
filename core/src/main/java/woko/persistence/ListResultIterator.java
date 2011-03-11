package woko.persistence;

import java.util.Iterator;
import java.util.List;

public class ListResultIterator implements ResultIterator {

  private final int start;
  private final int limit;
  private final int totalSize;
  private final Iterator delegate;

  public ListResultIterator(List objects, int start, int limit, int totalSize) {
    this(objects.iterator(), start, limit, totalSize);
  }

  public ListResultIterator(Iterator objects, int start, int limit, int totalSize) {
    this.start = start;
    this.limit = limit;
    this.totalSize = totalSize;
    delegate = objects;
  }

  public Object next() {
    return delegate.next();
  }

  public boolean hasNext() {
    return delegate.hasNext();
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