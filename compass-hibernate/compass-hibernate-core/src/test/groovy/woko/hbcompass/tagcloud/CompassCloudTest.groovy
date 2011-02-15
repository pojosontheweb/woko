package woko.hbcompass.tagcloud

import org.hibernate.Transaction
import org.hibernate.Session
import woko.hbcompass.HibernateCompassStore
import woko.hbcompass.entities.MySearchableEntity
import junit.framework.TestCase
import org.compass.core.Compass

class CompassCloudTest extends TestCase {

  HibernateCompassStore createStore() {
    def store = new HibernateCompassStore(['woko.hbcompass.entities'])
    doInTx(store) {
      for (int i=1;i<101;i++) {
        store.save(new MySearchableEntity([id:i,prop1:'funky',prop2:"tzeit$i"]))
      }
    }
    return store
  }

  private void doInTx(HibernateCompassStore store, Closure c) {
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

  void testCloud() {
    doInTx(createStore()) { HibernateCompassStore s ->
      Compass compass = s.compass
      Collection<CompassCloudElem> elems = new CompassCloud().getCloud(compass)
      assert elems
    }
  }
  

}
