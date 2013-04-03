package woko.ext.categories.hibernate;

import org.hibernate.criterion.Restrictions;
import woko.ext.categories.Category;
import woko.ext.categories.CategoryManager;
import woko.hibernate.HibernateStore;
import woko.util.Util;

import java.util.ArrayList;
import java.util.List;

public class HibernateCategoryManager implements CategoryManager {

    private final HibernateStore store;
    private final Class<?> categoryClass;

    public HibernateCategoryManager(HibernateStore store, Class<?> categoryClass) {
        this.store = store;
        this.categoryClass = categoryClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Category> getRootCategories() {
        // select all categories that don't have no parent
        return store.getSession().createCriteria(categoryClass)
                .add(Restrictions.isNull("parentCategory"))
                .list();
    }

    @Override
    public List<Category> getChoicesForParent(Category category, List<Category> defaultChoices) {
        Util.assertArg("category", category);
        List<Category> all = new ArrayList<Category>();
        if (defaultChoices!=null) {
            all.addAll(defaultChoices);
        }
        // remove the category itself from choices, as well as all children
        removeCategoriesRecursive(all, category);
        return all;
    }

    private void removeCategoriesRecursive(List<Category> all, Category category) {
        if (category!=null) {
            all.remove(category);
            List<? extends Category> subCategs = category.getSubCategories();
            if (subCategs!=null) {
                for (Category sub : category.getSubCategories()) {
                    removeCategoriesRecursive(all, sub);
                }
            }
        }
    }


    @Override
    public boolean moveCategory(Category category, boolean up) {
        Util.assertArg("category", category);
        if (up && !isMoveUpAllowed(category)) {
            return false;
        }
        if (!up && !isMoveDownAllowed(category)) {
            return false;
        }
        List<Category> siblings = siblings(category);
        if (siblings==null) {
            return false;
        }

        // re-assign all indexes
        int index = 0;
        for (Category sibling : siblings) {
            sibling.setSortIndex(index);
            store.save(sibling);
            index++;
        }

        // indexes found, swap
        int indexOfCateg = siblings.indexOf(category);
        int curIndex = category.getSortIndex();
        if (up) {
            Category previous = siblings.get(indexOfCateg-1);
            category.setSortIndex(previous.getSortIndex());
            previous.setSortIndex(curIndex);
            store.save(previous);
        } else {
            Category next = siblings.get(indexOfCateg+1);
            category.setSortIndex(next.getSortIndex());
            next.setSortIndex(curIndex);
            store.save(next);
        }
        store.save(category);

        return true;
    }

    private List<Category> siblings(Category category) {
        Category parent = category.getParentCategory();
        List<Category> siblings;
        if (parent==null) {
            siblings = getRootCategories();
        } else {
            siblings = parent.getSubCategories();
        }
        return siblings;
    }

    @Override
    public boolean isMoveUpAllowed(Category category) {
        List<? extends Category> siblings = siblings(category);
        if (siblings!=null) {
            return siblings.indexOf(category)>0;
        }
        return false;
    }

    @Override
    public boolean isMoveDownAllowed(Category category) {
        List<? extends Category> siblings = siblings(category);
        if (siblings!=null) {
            int index = siblings.indexOf(category);
            return index >= 0 && index < siblings.size() - 1;
        }
        return false;
    }
}