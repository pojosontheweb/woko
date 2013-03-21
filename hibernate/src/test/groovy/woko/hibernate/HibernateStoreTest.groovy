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

package woko.hibernate

import entities.MyEntityWithAlternateKey
import junit.framework.TestCase
import net.sourceforge.stripes.mock.MockServletContext
import woko.Woko
import woko.inmemory.InMemoryUserManager
import woko.ioc.SimpleWokoIocContainer
import woko.mock.MockUsernameResolutionStrategy
import woko.mock.MockUtil
import woko.persistence.ObjectStore
import entities.MyEntity
import woko.users.UserManager
import woko.util.LinkUtil

import static woko.mock.MockUtil.*

class HibernateStoreTest extends TestCase {

    private HibernateStore store

    protected void setUp() {
        store = new HibernateStore(['entities'])
    }

    private void doInTx(Closure c) {
        store.doInTx({st, sess ->
            c(st)
        } as TxCallback)
    }

    void testLoadNotExistingReturnsNull() {
        doInTx { s ->
            def e = s.load('MyEntity', '123')
            assert e == null
        }
    }

    void testSaveAndLoadAndDeleteInSameTx() {
        doInTx { ObjectStore s ->
            def e = new MyEntity([id: 123, name: "Moby"])
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
            def e = new MyEntity([id: 123, name: "Moby"])
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

    static Woko createWoko(String username) {
        HibernateStore store = new HibernateStore(["entities"])
        UserManager userManager = new InMemoryUserManager()
        userManager.addUser("wdevel", "wdevel", ["developer"])
        SimpleWokoIocContainer ioc = new SimpleWokoIocContainer(
                store,
                userManager,
                new MockUsernameResolutionStrategy(username),
                Woko.createFacetDescriptorManager(Woko.DEFAULT_FACET_PACKAGES))
        return new Woko(ioc, [Woko.ROLE_GUEST])
    }

    static def doWithMockContext(String username, Closure c) {
        new MockUtil().withServletContext(createWoko(username), { MockServletContext ctx ->
            c(ctx)
        } as MockUtil.Callback)
    }

    void testExceptionRollsBackTransaction() {
        try {
            doWithMockContext("wdevel") { MockServletContext c ->
                doInTx { s ->
                    MyEntity e = new MyEntity(id: 1, name: "foo")
                    s.save(e)
                }

                boolean thrown = false
                try {
                    tripAndGetFacet(c, "/saveAndThrow")
                    // should throw
                    fail("Should have thrown")
                } catch(Exception e) {
                    // normal behavior
                    thrown = true
                }
                assert thrown

                doInTx { s->
                    MyEntity e = s.load("MyEntity", "1")
                    assert e.name == 'foo'
                }
            }
        } finally {
            doInTx { s ->
                MyEntity e = s.load("MyEntity", "1")
                s.delete(e)
            }
        }
    }

    void testAlternateKey() {
        try {
            doWithMockContext("wdevel") { MockServletContext c ->
                doInTx { s ->
                    MyEntityWithAlternateKey e1 = new MyEntityWithAlternateKey(id: 1, name: "foo")
                    s.save(e1)
                    MyEntityWithAlternateKey e2 = new MyEntityWithAlternateKey(id: 2)
                    s.save(e2)
                }

                doInTx { s->
                    MyEntityWithAlternateKey e = s.load("MyEntityWithAlternateKey", "1")
                    assert e.name == 'foo'

                    e = s.load("MyEntityWithAlternateKey", "foo")
                    assert e
                    assert e.name == 'foo'

                    Woko woko = Woko.getWoko(c)
                    def link = LinkUtil.getUrl(woko, e, "view")
                    assert link == "view/MyEntityWithAlternateKey/foo"

                    MyEntityWithAlternateKey e2 = s.load("MyEntityWithAlternateKey", "2")
                    assert LinkUtil.getUrl(woko, e2, "view") == "view/MyEntityWithAlternateKey/2"
                }
            }
        } finally {
            doInTx { s ->
                MyEntityWithAlternateKey e1 = s.load("MyEntityWithAlternateKey", "1")
                s.delete(e1)
                MyEntityWithAlternateKey e2 = s.load("MyEntityWithAlternateKey", "2")
                s.delete(e2)
            }
        }
    }

    void testAlternateKeyWithDuplicates() {
        try {
            doWithMockContext("wdevel") { MockServletContext c ->
                doInTx { s ->
                    MyEntityWithAlternateKey e1 = new MyEntityWithAlternateKey(id: 1, name: "foo")
                    MyEntityWithAlternateKey e2 = new MyEntityWithAlternateKey(id: 2, name: "foo")
                    s.save(e1)
                    s.save(e2)
                }

                doInTx { s->
                    MyEntityWithAlternateKey e1 = s.load("MyEntityWithAlternateKey", "1")
                    MyEntityWithAlternateKey e2 = s.load("MyEntityWithAlternateKey", "2")
                    assert e1.name == 'foo'
                    assert e2.name == 'foo'

                    MyEntityWithAlternateKey e3 = s.load("MyEntityWithAlternateKey", "foo")
                    assert e3
                    assert e3.name == 'foo'
                    assert e3.id == 1

                    MyEntityWithAlternateKey e4 = s.load("MyEntityWithAlternateKey", "foo-2")
                    assert e4
                    assert e4.name == 'foo'
                    assert e4.id == 2

                    Woko woko = Woko.getWoko(c)
                    assert LinkUtil.getUrl(woko, e1, "view") == "view/MyEntityWithAlternateKey/foo"
                    assert LinkUtil.getUrl(woko, e2, "view") == "view/MyEntityWithAlternateKey/foo-2"
                }
            }
        } finally {
            doInTx { s ->
                MyEntityWithAlternateKey e = s.load("MyEntityWithAlternateKey", "1")
                s.delete(e)
            }
        }
    }

}
