package woko.inmemory

import woko.Woko
import net.sourceforge.stripes.mock.MockServletContext
import net.sourceforge.stripes.controller.StripesFilter
import net.sourceforge.stripes.controller.DispatcherServlet
import net.sourceforge.stripes.mock.MockRoundtrip
import javax.servlet.ServletException
import woko.facets.FacetNotFoundException
import net.sourceforge.stripes.action.ActionBean
import woko.facets.ResolutionFacet
import net.sourceforge.stripes.exception.StripesServletException

abstract class InMemRoundtripTestBase extends GroovyTestCase {

  Woko createWoko(String username) {
    Woko inMem = InMemoryWokoInitListener.doCreateWoko().setUsernameResolutionStrategy(new DummyURS(username: username))
    InMemoryObjectStore inMemObjectStore = inMem.objectStore
    inMemObjectStore.addObject("1", new Book([_id: '1', name: 'Moby Dick', nbPages: 123]))
    return inMem
  }

  MockServletContext createMockServletContext(String username) {
    Map<String, String> params = new HashMap<String, String>();
    params.put("ActionResolver.Packages", "woko.actions");
    params.put("Extension.Packages", "woko.actions");
    MockServletContext mockServletContext = new MockServletContext("basicroundtrip");
    mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
    mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
    Woko woko = createWoko(username)
    mockServletContext.setAttribute Woko.CTX_KEY, woko
    return mockServletContext;
  }

  def trip(Class<? extends ActionBean> expectedClass, String username, String facetName, String className, String key) {
    return trip(expectedClass, username, facetName, className, key, null)
  }

  def trip(Class<? extends ActionBean> expectedClass, String username, String facetName, String className, String key, Map params) {
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
      params.each { k, v ->
        t.addParameter(k, v)
      }
    }
    t.execute()
    ActionBean ab = t.getActionBean(expectedClass)
    assert ab
    return ab
  }

  void assertFacetNotFound(String username, String facetName, String className, String key) {
    boolean hasThrown = false
    try {
      trip(ResolutionFacet.class, username, facetName, className, key)
    } catch (Exception e) {
      hasThrown = e instanceof StripesServletException
    }
    assert hasThrown
  }


}
