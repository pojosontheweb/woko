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

package woko.persistence;

import java.util.Iterator;
import java.util.List;

/**
 * List-based <code>ResultIterator</code> implementation.
 * @param <T> the type of iterator objects
 */
public class ListResultIterator<T> extends ResultIterator<T> {

    private final int start;
    private final int limit;
    private final int totalSize;
    private final Iterator<? extends T> delegate;

    /**
     * Create the iterator with passed parameters
     * @param objects the list of objects to be used
     * @param start the start offset
     * @param limit the limit
     * @param totalSize the total size
     */
    public ListResultIterator(List<? extends T> objects, int start, int limit, int totalSize) {
        this(objects.iterator(), start, limit, totalSize);
    }

    /**
     * Create the iterator with passed parameters
     * @param objects an iterator of objects to be used
     * @param start the start offset
     * @param limit the limit
     * @param totalSize the total size
     */
    public ListResultIterator(Iterator<? extends T> objects, int start, int limit, int totalSize) {
        this.start = start;
        this.limit = limit;
        this.totalSize = totalSize;
        delegate = objects;
    }

    /**
     * Return the next object if any
     * @return the next object if any
     */
    public T next() {
        return delegate.next();
    }

    /**
     * Return <code>true</code> if the iterator has more elements, <code>false</code> otherwise
     * @return <code>true</code> if the iterator has more elements, <code>false</code> otherwise
     */
    public boolean hasNext() {
        return delegate.hasNext();
    }

    /**
     * Return the start offset, as passed at construction time
     * @return the start offset, as passed at construction time
     */
    public int getStart() {
        return start;
    }

    /**
     * Return the limit, as passed at construction time
     * @return the limit, as passed at construction time
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Return the total size, as passed at construction time
     * @return the total size, as passed at construction time
     */
    public int getTotalSize() {
        return totalSize;
    }

}