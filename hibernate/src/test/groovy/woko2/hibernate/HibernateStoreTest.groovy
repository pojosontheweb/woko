package woko2.hibernate

import junit.framework.TestCase
import org.hibernate.Session
import org.hibernate.Transaction
import woko2.persistence.ObjectStore
import entities.MyEntity
import woko2.persistence.ResultIterator

class HibernateStoreTest extends TestCase {

  private HibernateStore store

  protected void setUp() {
    store = new HibernateStore(['entities'])
  }

  private void doInTx(Closure c) {
    Session s = store.getSessionFactory().getCurrentSession()
    Transaction tx = s.beginTransaction()
    println "Started tx $tx"
    try {
      c.call(store)
      println "Commiting  $tx"
      tx.commit()
    } catch(Exception e) {
      println "Exception caught : $e, Roll-backing and rethrowing"
      tx.rollback()
      throw new RuntimeException("Exception within tx", e)
    }
  }

  void testLoadNotExistingReturnsNull() {
    doInTx { s ->
      def e = s.load('MyEntity', '123')
      assert e == null
    }
  }

  void testSaveAndLoadAndDeleteInSameTx() {
    doInTx { ObjectStore s ->
      def e = new MyEntity([id:123,name:"Moby"])
      s.save(e)
      e = s.load("MyEntity", "123")
      assert e.name == "Moby"
      s.delete(e)
      e = s.load("MyEntity", "123")
      assert e == null
    }
  }

  void testSaveAndLoadAndDeleteInDifferentTx() {
    doInTx { s ->
      def e = new MyEntity([id:123,name:"Moby"])
      s.save(e)
    }
    doInTx { s ->
      def e = s.load("MyEntity", "123")
      assert e.name == "Moby"
      s.delete(e)
    }
    doInTx { s ->
      def e = s.load("MyEntity", "123")
      assert e == null
    }
  }

  void testSearch() {
    // save a bunch of objects in tx
    doInTx { s->
      for (int i=0;i<50;i++) {
        s.save(new MyEntity([id:i,name:"Moby word$i"]))
      }
    }

    // search
    doInTx { s->
      ResultIterator r = s.search("Moby", null, null)
      assert r.totalSize == 50
    }
    doInTx { s->
      ResultIterator r = s.search("Moby", 0, 10)
      assert r.totalSize == 50
      def results = []
      while (r.hasNext()) {
        results << r.next() 
      }
      assert results.size() == 10
    }

    // delete objects
    doInTx { s->
      for (int i=0;i<50;i++) {
        def o = s.load('MyEntity', "$i")
        s.delete(o)
      }
    }

    // make sure search is updated
    doInTx { s->
      ResultIterator r = s.search("Moby", null, null)
      assert r.totalSize == 0
    }
  }

}
