/*
 * Copyright 2001-2010 Remi Vankeisbelck
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
