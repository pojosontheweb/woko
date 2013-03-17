package woko.ext.categories;

import woko.persistence.ResultIterator;

public interface CategoryManager {

    static final String KEY = "CategoryManager";

    void setParentCategory(Category child, Category newParent);

    ResultIterator<? extends Categorizable> listObjectsInCategory(Category category, Integer start, Integer limit);

}
