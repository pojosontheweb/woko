package woko.ext.categories;

import woko.persistence.ResultIterator;

import java.util.List;

public interface CategoryManager {

    static final String KEY = "CategoryManager";

    List<Category> getRootCategories();

}
