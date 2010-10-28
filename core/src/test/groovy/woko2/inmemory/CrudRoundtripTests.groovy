package woko2.inmemory

import junit.framework.TestCase
import net.sourceforge.stripes.controller.DispatcherServlet
import net.sourceforge.stripes.controller.StripesFilter
import net.sourceforge.stripes.mock.MockServletContext
import net.sourceforge.stripes.mock.MockRoundtrip
import woko2.actions.WokoActionBean
import woko2.Woko
import woko2.facets.FacetNotFoundException
import woko2.facets.builtin.developer.View
import javax.servlet.ServletException

class CrudRoundtripTests extends TestCase {

  Woko createWoko(String username) {
    Woko inMem = new InMemoryWoko([Woko.ROLE_GUEST]).setUsernameResolutionStrategy(new DummyURS(username:username))
    InMemoryObjectStore inMemObjectStore = inMem.objectStore
    inMemObjectStore.addObject('Book', '1', new Book([name:'Moby Dick',nbPages:123]))
    return inMem
  }

  private MockServletContext createMockServletContext(String username) {
    Map<String,String> params = new HashMap<String,String>();
    params.put("ActionResolver.Packages", "woko2.actions");
    params.put("Extension.Packages", "woko2.actions");
    MockServletContext mockServletContext = new MockServletContext("basicroundtrip");
    mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
    mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
    Woko woko = createWoko(username)
    mockServletContext.setAttribute Woko.CTX_KEY, woko
    return mockServletContext;
  }

  private WokoActionBean trip(String username, String facetName, String className, String key) {
    def c = createMockServletContext(username)
    StringBuilder url = new StringBuilder('/').append(facetName)
    if (className) {
      url << '/'
      url << className
    }
    if (key) {
      url << '/'
      url << key
    }
    MockRoundtrip t = new MockRoundtrip(c, url.toString())
    t.execute()
    WokoActionBean ab = t.getActionBean(WokoActionBean.class)
    assert ab
    return ab
  }

  void assertFacetNotFound(String username, String facetName, String className, String key) {
    boolean hasThrown = false
    try {
      trip(username, facetName, className, key)
    } catch(Exception e) {
      if (e instanceof ServletException) {
        hasThrown = e.cause instanceof FacetNotFoundException
      }
    }
    assert hasThrown
  }

  void testGuestViewNoObject() {
    assertFacetNotFound(null, 'view', null, null)
  }

  void testGuestViewBook() {
    assertFacetNotFound(null, 'view', 'Book', '1')
  }

  void testDeveloperViewNoObject() {
    assertFacetNotFound('wdevel', 'view', null, null)
  }

  void testDeveloperViewBook() {
    WokoActionBean ab = trip('wdevel', 'view','Book', '1')
    assert ab.facet.getClass() == View.class
  }

}
