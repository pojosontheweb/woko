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

package woko.hbcompass

import junit.framework.TestCase
import woko.persistence.ResultIterator
import org.hibernate.Transaction
import org.hibernate.Session
import woko.hbcompass.entities.MySearchableEntity

class HibernateCompassStoreTest extends TestCase {

  HibernateCompassStore createStore() {
    def store = new HibernateCompassStore(['woko.hbcompass.entities'])
    doInTx(store) {
      for (int i=1;i<101;i++) {
        store.save(new MySearchableEntity([id:i,prop1:'foo',prop2:"bar$i"]))
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

  void testSearch() {
    HibernateCompassStore store = createStore()
    doInTx(store) {
      ResultIterator r = store.search('bar10', 0, -1)
      assert r.totalSize == 1
      assert r.hasNext()
      def hit0 = r.next()
      assert hit0.id == 10
      assert hit0.prop1 == 'foo'
      assert hit0.prop2 == 'bar10'
      assert !r.hasNext()
    }

    doInTx(store) {
      ResultIterator r = store.search('bar10 foo', 0, -1)
      assert r.totalSize == 1
      assert r.hasNext()
      def hit0 = r.next()
      assert hit0.id == 10
      assert hit0.prop1 == 'foo'
      assert hit0.prop2 == 'bar10'
      assert !r.hasNext()
    }

    doInTx(store) {
      ResultIterator r = store.search('foo', 0, -1)
      assert r.totalSize == 100
      for (int i=0;i<100;i++) {
        assert r.hasNext()
        r.next()
      }
      assert !r.hasNext()
    }

    doInTx(store) {
      ResultIterator r = store.search('foo', 0, 10)
      assert r.totalSize == 100
      for (int i=0 ; i<10 ; i++) {
        assert r.hasNext()
        def obj = r.next()
        assert obj.prop1 == 'foo'
      }
      assert !r.hasNext()
    }

  }

}
