package woko.ext.categories;

import java.util.List;

public interface Category {

    Long getId();

    String getName();

    Category getParentCategory();

    List<? extends Category> getSubCategories();

    Integer getIndex();

}
