package woko.hibernate

import junit.framework.TestCase
import org.hibernate.Session
import org.hibernate.Transaction
import woko.persistence.ObjectStore
import entities.MyEntity

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
    } catch(Throwable e) {
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

}
