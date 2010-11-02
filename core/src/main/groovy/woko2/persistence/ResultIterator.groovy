package woko2.persistence

public interface ResultIterator {

  Object next()

  boolean hasNext()

  int getStart()

  int getLimit()

  int getTotalSize()

}