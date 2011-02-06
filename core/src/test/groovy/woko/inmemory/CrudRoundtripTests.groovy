package woko.inmemory

import junit.framework.TestCase
import net.sourceforge.stripes.controller.DispatcherServlet
import net.sourceforge.stripes.controller.StripesFilter
import net.sourceforge.stripes.mock.MockServletContext
import net.sourceforge.stripes.mock.MockRoundtrip
import woko.actions.WokoActionBean
import woko.Woko
import woko.facets.FacetNotFoundException
import woko.facets.builtin.developer.ViewImpl
import javax.servlet.ServletException
import woko.facets.builtin.developer.SaveImpl
import woko.facets.builtin.developer.DeleteImpl

class CrudRoundtripTests extends TestCase {

  Woko createWoko(String username) {
    Woko inMem = InMemoryWokoInitListener.doCreateWoko().setUsernameResolutionStrategy(new DummyURS(username:username))
    InMemoryObjectStore inMemObjectStore = inMem.objectStore
    inMemObjectStore.addObject(new Book([_id:'1',name:'Moby Dick',nbPages:123]))
    return inMem
  }

  private MockServletContext createMockServletContext(String username) {
    Map<String,String> params = new HashMap<String,String>();
    params.put("ActionResolver.Packages", "woko.actions");
    params.put("Extension.Packages", "woko.actions");
    MockServletContext mockServletContext = new MockServletContext("basicroundtrip");
    mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
    mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
    Woko woko = createWoko(username)
    mockServletContext.setAttribute Woko.CTX_KEY, woko
    return mockServletContext;
  }

  private WokoActionBean trip(String username, String facetName, String className, String key) {
    return trip(username, facetName, className, key, null)
  }

  private WokoActionBean trip(String username, String facetName, String className, String key, Map params) {
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
    if (params) {
      params.each { k,v ->
        t.addParameter(k,v)
      }
    }
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
    assert ab.facet.context.woko.objectStore.load('woko.inmemory.Book', '1') == null
  }


}
