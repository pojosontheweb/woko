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

import net.sourceforge.stripes.mock.MockServletContext
import woko.facets.builtin.developer.ViewImpl
import woko.facets.builtin.developer.SaveImpl
import woko.facets.builtin.developer.DeleteImpl
import javax.servlet.http.HttpServletRequest
import static woko.mock.MockUtil.*

class CrudRoundtripTest extends InMemRoundtripTestBase {

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
        doWithMockContext("wdevel") { MockServletContext c ->
            def f = tripAndGetFacet(c, 'view', 'woko.inmemory.Book', '1')
            assert f.getClass() == ViewImpl.class
        }
    }

    void testDeveloperUpdateBook() {
        doWithMockContext("wdevel") { MockServletContext c ->
            def wab = tripAndGetWokoActionBean(c, 'save', 'woko.inmemory.Book', '1', ['object.name': 'New name'])
            assert wab.facet.getClass() == SaveImpl.class
            assert wab.object.name == 'New name'
        }
    }

    void testDeveloperDeleteBook() {
        doWithMockContext("wdevel") { MockServletContext c ->
            def wab = tripAndGetWokoActionBean(c, 'delete', 'woko.inmemory.Book', '1', ['facet.confirm': 'true'])
            assert wab.facet.getClass() == DeleteImpl.class
            assert wab.facet.facetContext.woko.objectStore.load('woko.inmemory.Book', '1') == null
        }
    }

    void testFacetContextHasRequest() {
        doWithMockContext("wdevel") { MockServletContext c ->
            def wab = tripAndGetWokoActionBean(c, 'view', 'woko.inmemory.Book', '1')
            def f = wab.facet
            assert f
            assert f.facetContext.request instanceof HttpServletRequest
        }
    }


}
