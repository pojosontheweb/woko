package woko.ext.categories;

import java.util.List;

public interface CategoryManager {

    static final String KEY = "CategoryManager";

    List<Category> getRootCategories();

    List<Category> getChoicesForParent(Category category, List<Category> defaultChoices);

    boolean isMoveUpAllowed(Category category);

    boolean isMoveDownAllowed(Category category);

    public boolean moveCategory(Category category, boolean up);

}
