package woko.inmemory

import junit.framework.TestCase
import net.sourceforge.stripes.controller.DispatcherServlet
import net.sourceforge.stripes.controller.StripesFilter
import net.sourceforge.stripes.mock.MockRoundtrip
import net.sourceforge.stripes.mock.MockServletContext
import woko.Woko
import woko.actions.WokoActionBean

class TypeConverterRoundtripTests extends TestCase {

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

  void testWokoTypeConverter() {
    def c = createMockServletContext('wdevel')
    MockRoundtrip trip = new MockRoundtrip(c, '/testMe')
    trip.addParameter('facet.book','1')
    trip.execute()
    WokoActionBean ab = trip.getActionBean(WokoActionBean.class)
    def f = ab.facet
    assert f.book.name == 'Moby Dick'
  }



}
