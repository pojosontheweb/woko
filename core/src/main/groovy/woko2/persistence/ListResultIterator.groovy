package woko2.persistence

class ListResultIterator implements ResultIterator {

  private final int start
  private final int limit
  private final int totalSize
  private final List objects
  private final Iterator delegate

  def ListResultIterator(List objects, int start, int limit, int totalSize) {
    this.objects = Collections.unmodifiableList(objects)
    this.start = start
    this.limit = limit
    this.totalSize = totalSize
    delegate = objects.iterator()
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