package woko.ext.categories.hibernate;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import woko.ext.categories.Categorizable;
import woko.ext.categories.Category;
import woko.ext.categories.CategoryManager;
import woko.hibernate.HibernateStore;
import woko.persistence.ResultIterator;
import woko.util.Util;

import java.util.ArrayList;
import java.util.List;

public class HibernateCategoryManager implements CategoryManager {

    private final HibernateStore store;

    public HibernateCategoryManager(HibernateStore store) {
        this.store = store;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Category> getRootCategories() {
        // select all categories that don't have no parent
        return store.getSession().createCriteria(HibernateCategory.class)
                .add(Restrictions.isNull("parent"))
                .list();
    }
}
