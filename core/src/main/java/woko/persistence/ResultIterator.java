package woko.persistence;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ResultIterator<T> {

    public abstract T next();

    public abstract boolean hasNext();

    public abstract int getStart();

    public abstract int getLimit();

    public abstract int getTotalSize();

    public List<T> toList() {
        ArrayList<T> result = new ArrayList<T>();
        while (hasNext()) {
            result.add(next());
        }
        return Collections.unmodifiableList(result);
    }

}