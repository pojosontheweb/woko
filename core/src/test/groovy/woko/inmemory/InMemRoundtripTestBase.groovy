package woko.inmemory

import woko.Woko
import net.sourceforge.stripes.mock.MockServletContext
import net.sourceforge.stripes.controller.StripesFilter
import net.sourceforge.stripes.controller.DispatcherServlet
import woko.actions.WokoActionBean
import net.sourceforge.stripes.mock.MockRoundtrip
import javax.servlet.ServletException
import woko.facets.FacetNotFoundException

abstract class InMemRoundtripTestBase extends GroovyTestCase {

  Woko createWoko(String username) {
    Woko inMem = InMemoryWokoInitListener.doCreateWoko().setUsernameResolutionStrategy(new DummyURS(username: username))
    InMemoryObjectStore inMemObjectStore = inMem.objectStore
    inMemObjectStore.addObject(new Book([_id: '1', name: 'Moby Dick', nbPages: 123]))
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

  WokoActionBean trip(String username, String facetName, String className, String key) {
    return trip(username, facetName, className, key, null)
  }

  WokoActionBean trip(String username, String facetName, String className, String key, Map params) {
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
    WokoActionBean ab = t.getActionBean(WokoActionBean.class)
    assert ab
    return ab
  }

  void assertFacetNotFound(String username, String facetName, String className, String key) {
    boolean hasThrown = false
    try {
      trip(username, facetName, className, key)
    } catch (Exception e) {
      if (e instanceof ServletException) {
        hasThrown = e.cause instanceof FacetNotFoundException
      }
    }
    assert hasThrown
  }


}
