/*
 * Copyright 2001-2012 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
