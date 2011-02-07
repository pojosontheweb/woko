package woko.persistence

class ListResultIterator implements ResultIterator {

  private final int start
  private final int limit
  private final int totalSize
  private final Iterator delegate

  def ListResultIterator(List objects, int start, int limit, int totalSize) {
    this(objects.iterator(), start, limit, totalSize)
  }

  def ListResultIterator(Iterator objects, int start, int limit, int totalSize) {
    this.start = start
    this.limit = limit
    this.totalSize = totalSize
    delegate = objects
  }

  Object next() {
    return delegate.next()
  }

  boolean hasNext() {
    return delegate.hasNext()
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