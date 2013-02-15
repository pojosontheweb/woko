/*
 * Copyright 2001-2012 Remi Vankeisbelck
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

package woko.hbcompass;

import org.compass.core.CompassHit;
import org.compass.core.CompassHitsOperations;
import woko.persistence.ResultIterator;

import java.util.Iterator;

/**
 * ResultIterator implementation backed by <code>CompassHitsOperations</code> object.
 */
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
