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

package woko.inmemory

import junit.framework.TestCase
import woko.Woko
import net.sourceforge.stripes.mock.MockHttpServletRequest
import woko.facets.builtin.developer.ViewImpl
import woko.facets.builtin.all.LogoutImpl
import woko.users.UserManager

class InMemoryWokoFacetsTest extends TestCase {

  Woko createWoko(String username) {
    InMemoryObjectStore store = new InMemoryObjectStore()
    store.addObject('1', new Book([_id: '1', name: 'Moby Dick', nbPages: 123]))
    UserManager userManager = new InMemoryUserManager()
    userManager.addUser("wdevel", "wdevel", ["developer"])
    Woko inMem = new Woko(
        store,
        userManager,
        [Woko.ROLE_GUEST],
        Woko.createFacetDescriptorManager(Woko.DEFAULT_FACET_PACKAGES),
        new DummyURS(username:username));

    return inMem
  }

  def getFacet(String facetName, String username, Object targetObject) {
    return createWoko(username).getFacet(facetName, new MockHttpServletRequest('',''), targetObject)
  }

  void assertFacetClass(Class expectedClass, String facetName, String username, Object targetObject) {
    def facet = createWoko(username).getFacet(facetName, new MockHttpServletRequest('',''), targetObject)
    assert facet?.getClass() == expectedClass
  }

  void testLogoutNoUserNoObject() {
    assertFacetClass(LogoutImpl.class, 'logout', null, null)
  }

  void testLogoutNoUserDummyObject() {
    assertFacetClass(LogoutImpl.class, 'logout', null, [foo:'bar'])
  }

  void testLogoutDeveloperNoObject() {
    assertFacetClass(LogoutImpl.class, 'logout', 'wdevel', null)
  }

  void testLogoutDeveloperDummyObject() {
    assertFacetClass(LogoutImpl.class, 'logout', 'wdevel', [foo:'bar'])
  }

  void testViewNoUserNoObject() {
    def f = getFacet('view', null, null)
    assert f==null
  }

  void testViewNoUserDummyObject() {
    def f = getFacet('view', null, [foo:'bar'])
    assert f==null
  }

  void testViewDevelNoObject() {
    def f = getFacet('view', 'wdevel', null)
    assert f==null
  }

  void testViewDevelDummyObject() {
    assertFacetClass(ViewImpl.class, 'view', 'wdevel', [foo:'bar'])
  }

}
