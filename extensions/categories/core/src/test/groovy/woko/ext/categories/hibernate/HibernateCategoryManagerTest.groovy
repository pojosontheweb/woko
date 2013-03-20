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
        HibernateCategory reading
        doInTx {
            reading = new HibernateCategory(name: "Reading")
            store.save(reading)

        }

        HibernateCategory books
        doInTx {
            reading = store.session.get(HibernateCategory.class, reading.id)
            books = new HibernateCategory(name: "Books", parentCategory: reading)
            store.save(books)
        }

        doInTx {
            reading = store.session.get(HibernateCategory.class, reading.id)
            books = store.session.get(HibernateCategory.class, books.id)
            assert books.parentCategory == reading
            assert reading.subCategories.size()==1
            assert reading.subCategories[0] == books
        }

        HibernateCategory articles
        doInTx {
            reading = store.session.get(HibernateCategory.class, reading.id)
            articles = new HibernateCategory(name: "Articles", parentCategory: reading)
            store.save(articles)
        }

        doInTx {
            reading = store.session.get(HibernateCategory.class, reading.id)
            books = store.session.get(HibernateCategory.class, books.id)
            articles = store.session.get(HibernateCategory.class, articles.id)

            assert articles.parentCategory == reading
            assert reading.subCategories.size()==2
            assert reading.subCategories[0] == books
            assert reading.subCategories[1] == articles

            List<Category> rootCategs = categoryManager.rootCategories
            assert rootCategs.size()==1
            assert rootCategs[0] == reading
        }

        // sorting tests
        doInTx {
            reading = store.session.get(HibernateCategory.class, reading.id)
            books = store.session.get(HibernateCategory.class, books.id)
            articles = store.session.get(HibernateCategory.class, articles.id)

            assert reading.subCategories[0] == books
            assert reading.subCategories[1] == articles

            assert categoryManager.isMoveDownAllowed(books)
            assert !categoryManager.isMoveUpAllowed(books)
            assert categoryManager.isMoveUpAllowed(articles)
            assert !categoryManager.isMoveDownAllowed(articles)

            categoryManager.moveCategory(books, false)
        }

        doInTx {
            reading = store.session.get(HibernateCategory.class, reading.id)
            books = store.session.get(HibernateCategory.class, books.id)
            articles = store.session.get(HibernateCategory.class, articles.id)

            assert reading.subCategories[0] == articles
            assert reading.subCategories[1] == books

            assert categoryManager.isMoveDownAllowed(articles)
            assert !categoryManager.isMoveUpAllowed(articles)
            assert categoryManager.isMoveUpAllowed(books)
            assert !categoryManager.isMoveDownAllowed(books)

            categoryManager.moveCategory(reading, true)
        }

        doInTx {
            reading = store.session.get(HibernateCategory.class, reading.id)
            books = store.session.get(HibernateCategory.class, books.id)
            articles = store.session.get(HibernateCategory.class, articles.id)

            assert reading.subCategories[0] == books
            assert reading.subCategories[1] == articles

            assert categoryManager.isMoveDownAllowed(books)
            assert !categoryManager.isMoveUpAllowed(books)
            assert categoryManager.isMoveUpAllowed(articles)
            assert !categoryManager.isMoveDownAllowed(articles)
        }
    }
}
