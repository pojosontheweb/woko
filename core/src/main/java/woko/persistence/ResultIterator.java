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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * ResultIterator allows for paginated lists.
 * @param <T> the type of elements returned by the iterator
 */
public abstract class ResultIterator<T> implements Iterator<T> {

    /**
     * Return the next element if any
     * @return the next element if any
     */
    public abstract T next();

    /**
     * Return <code>true</code> if the iterator has more elements, <code>false</code> otherwise
     * @return <code>true</code> if the iterator has more elements, <code>false</code> otherwise
     */
    public abstract boolean hasNext();

    /**
     * Return the start offset (used for pagination of the results)
     * @return the start offset
     */
    public abstract int getStart();

    /**
     * Return the limit (used for pagination of the results)
     * @return the limit
     */
    public abstract int getLimit();

    /**
     * Return the total size (used for pagination of the results)
     * @return the total size
     */
    public abstract int getTotalSize();

    /**
     * Convert iterator elements to a <code>List</code>
     * @return a List of elements in the iterator
     */
    public List<T> toList() {
        ArrayList<T> result = new ArrayList<T>();
        while (hasNext()) {
            result.add(next());
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}