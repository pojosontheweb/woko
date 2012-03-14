/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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