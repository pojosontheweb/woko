package woko2.inmemory

import junit.framework.TestCase
import woko2.facets.builtin.all.Logout
import woko2.Woko
import net.sourceforge.stripes.mock.MockHttpServletRequest
import woko2.facets.FacetConstants
import woko2.facets.builtin.developer.View

class InMemoryWokoFacetsTest extends TestCase {

  Woko createWoko(String username) {
    return new InMemoryWoko([Woko.ROLE_GUEST]).setUsernameResolutionStrategy(new DummyURS(username:username))
  }

  def getFacet(String facetName, String username, Object targetObject) {
    return createWoko(username).getFacet(facetName, new MockHttpServletRequest('',''), targetObject, targetObject?.getClass())
  }

  void assertFacetClass(Class expectedClass, String facetName, String username, Object targetObject) {
    def facet = createWoko(username).getFacet(facetName, new MockHttpServletRequest('',''), targetObject, targetObject?.getClass())
    assert facet?.getClass() == expectedClass
  }

  void testLogoutNoUserNoObject() {
    assertFacetClass(Logout.class, FacetConstants.logout, null, null)
  }

  void testLogoutNoUserDummyObject() {
    assertFacetClass(Logout.class, FacetConstants.logout, null, [foo:'bar'])
  }

  void testLogoutDeveloperNoObject() {
    assertFacetClass(Logout.class, FacetConstants.logout, 'wdevel', null)
  }

  void testLogoutDeveloperDummyObject() {
    assertFacetClass(Logout.class, FacetConstants.logout, 'wdevel', [foo:'bar'])
  }

  void testViewNoUserNoObject() {
    def f = getFacet(FacetConstants.view, null, null)
    assert f==null
  }

  void testViewNoUserDummyObject() {
    def f = getFacet(FacetConstants.view, null, [foo:'bar'])
    assert f==null
  }

  void testViewDevelNoObject() {
    def f = getFacet(FacetConstants.view, 'wdevel', null)
    assert f==null
  }

  void testViewDevelDummyObject() {
    assertFacetClass(View.class, FacetConstants.view, 'wdevel', [foo:'bar'])
  }  

}
