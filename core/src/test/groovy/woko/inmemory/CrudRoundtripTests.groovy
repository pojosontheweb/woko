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

package woko.inmemory

import woko.actions.WokoActionBean
import woko.facets.builtin.developer.ViewImpl
import woko.facets.builtin.developer.SaveImpl
import woko.facets.builtin.developer.DeleteImpl
import javax.servlet.http.HttpServletRequest

class CrudRoundtripTests extends InMemRoundtripTestBase {

  void testGuestViewNoObject() {
    assertFacetNotFound(null, 'view', null, null)
  }

  void testGuestViewBook() {
    assertFacetNotFound(null, 'view', 'woko.inmemory.Book', '1')
  }

  void testDeveloperViewNoObject() {
    assertFacetNotFound('wdevel', 'view', null, null)
  }

  void testDeveloperViewBook() {
    WokoActionBean ab = trip('wdevel', 'view','woko.inmemory.Book', '1')
    assert ab.facet.getClass() == ViewImpl.class
  }

  void testDeveloperUpdateBook() {
    WokoActionBean ab = trip('wdevel', 'save','woko.inmemory.Book', '1', ['object.name':'New name'])
    assert ab.facet.getClass() == SaveImpl.class
    assert ab.object.name=='New name'
  }

  void testDeveloperDeleteBook() {
    WokoActionBean ab = trip('wdevel', 'delete','woko.inmemory.Book', '1', ['facet.confirm':'true'])
    assert ab.facet.getClass() == DeleteImpl.class
    assert ab.facet.facetContext.woko.objectStore.load('woko.inmemory.Book', '1') == null
  }

  void testFacetContextHasRequest() {
    WokoActionBean ab = trip('wdevel', 'view','woko.inmemory.Book', '1')
    def f = ab.facet
    assert f
    assert f.facetContext.request instanceof HttpServletRequest
  }



}
