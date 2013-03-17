package woko.ext.categories.hibernate

import junit.framework.TestCase
import woko.hibernate.HibernateStore
import woko.persistence.TransactionCallback

class HibernateCategoryManagerTest extends TestCase {

    private HibernateStore store
    private HibernateCategoryManager categoryManager

    @Override
    protected void setUp() {
        store = new HibernateStore(['woko.ext.categories.hibernate'])
        categoryManager = new HibernateCategoryManager(store)
    }

    @Override
    protected void tearDown() throws Exception {
        store.close()
    }

    private void doInTx(Closure c) {
        store.doInTransaction({
            c()
        } as TransactionCallback)
    }


    void testHibernateCategoryManager() {
        doInTx {
            HibernateCategory reading = new HibernateCategory(name: "Reading")
            store.save(reading)

            HibernateCategory books = new HibernateCategory(name: "Books")
            store.save(books)
            categoryManager.setParentCategory(books, reading);

            assert books.parentCategory == reading
            assert reading.subCategories.size()==1
            assert reading.subCategories[0] == books

            HibernateCategory articles = new HibernateCategory(name: "Articles")
            store.save(articles)
            categoryManager.setParentCategory(articles, reading);

            assert articles.parentCategory == reading
            assert reading.subCategories.size()==2
            assert reading.subCategories[0] == books
            assert reading.subCategories[1] == articles
        }

    }
}
