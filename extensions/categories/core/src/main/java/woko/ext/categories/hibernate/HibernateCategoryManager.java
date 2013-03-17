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
    public void setParentCategory(Category child, Category newParent) {
        Util.assertArg("child", child);

        HibernateCategory hbChild = (HibernateCategory)child;
        HibernateCategory hbParent = (HibernateCategory)newParent;

        // cleanup old parent if any...
        HibernateCategory oldParent = hbChild.getParentCategory();
        if (oldParent!=null) {
            @SuppressWarnings("unchecked")
            List<HibernateCategory> oldSubCategs = (List<HibernateCategory>)oldParent.getSubCategories();
            if (oldSubCategs!=null) {
                if (oldSubCategs.remove(child)) {
                    store.save(oldParent);
                }
            }
        }

        // now assign new parent
        hbChild.setParentCategory(hbParent);
        store.save(hbChild);
        if (hbParent!=null) {
            @SuppressWarnings("unchecked")
            List<HibernateCategory> subCategs = (List<HibernateCategory>)hbParent.getSubCategories();
            if (subCategs==null) {
                subCategs = new ArrayList<HibernateCategory>();
                hbParent.setSubCategories(subCategs);
            }
            subCategs.add(hbChild);
            store.save(hbParent);
        }
    }

    @Override
    public ResultIterator<? extends Categorizable> listObjectsInCategory(Category category, Integer start, Integer limit) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
