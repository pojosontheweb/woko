package woko.persistence;

import java.util.Iterator;
import java.util.List;

public class ListResultIterator<T> extends ResultIterator<T> {

  private final int start;
  private final int limit;
  private final int totalSize;
  private final Iterator<? extends T> delegate;

  public ListResultIterator(List<? extends T> objects, int start, int limit, int totalSize) {
    this(objects.iterator(), start, limit, totalSize);
  }

  public ListResultIterator(Iterator<? extends T> objects, int start, int limit, int totalSize) {
    this.start = start;
    this.limit = limit;
    this.totalSize = totalSize;
    delegate = objects;
  }

  public T next() {
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