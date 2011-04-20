package woko.inmemory

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
    trip(ViewImpl.class, 'wdevel', 'view','woko.inmemory.Book', '1')
  }

  void testDeveloperUpdateBook() {
    def ab = trip(SaveImpl.class, 'wdevel', 'save','woko.inmemory.Book', '1', ['facetContext.targetObject.name':'New name'])
    assert ab.facetContext.targetObject.name=='New name'
  }

  void testDeveloperDeleteBook() {
    def ab = trip(DeleteImpl.class, 'wdevel', 'delete','woko.inmemory.Book', '1', ['confirm':'true'])
    assert ab.facetContext.woko.objectStore.load('woko.inmemory.Book', '1') == null
  }

  void testFacetContextHasRequest() {
    def ab = trip(ViewImpl.class, 'wdevel', 'view','woko.inmemory.Book', '1')
    assert ab
    assert ab.facetContext.request instanceof HttpServletRequest
  }



}
