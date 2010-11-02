package woko2.persistence

public interface ResultIterator<T> {

  T next()

  boolean hasNext()

  int getStart()

  int getLimit()

  int getTotalSize()

}