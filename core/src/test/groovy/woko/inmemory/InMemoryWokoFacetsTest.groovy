package woko.inmemory

import junit.framework.TestCase
import woko.Woko
import net.sourceforge.stripes.mock.MockHttpServletRequest
import woko.facets.builtin.developer.ViewImpl
import woko.facets.builtin.all.LogoutImpl

class InMemoryWokoFacetsTest extends TestCase {

  Woko createWoko(String username) {
    return InMemoryWokoInitListener.doCreateWoko().setUsernameResolutionStrategy(new DummyURS(username:username))
  }

  def getFacet(String facetName, String username, Object targetObject) {
    return createWoko(username).getFacet(facetName, new MockHttpServletRequest('',''), targetObject, targetObject?.getClass())
  }

  void assertFacetClass(Class expectedClass, String facetName, String username, Object targetObject) {
    def facet = createWoko(username).getFacet(facetName, new MockHttpServletRequest('',''), targetObject, targetObject?.getClass())
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
