package woko2.inmemory

import junit.framework.TestCase
import net.sourceforge.stripes.controller.DispatcherServlet
import net.sourceforge.stripes.controller.StripesFilter
import net.sourceforge.stripes.mock.MockServletContext
import net.sourceforge.stripes.mock.MockRoundtrip
import woko2.actions.WokoActionBean
import woko2.facets.builtin.developer.Save
import woko2.Woko

class CrudRoundtripTests extends TestCase {

  private MockServletContext createMockServletContext() {
    Map<String,String> params = new HashMap<String,String>();
    params.put("ActionResolver.Packages", "woko2.actions");
    params.put("Extension.Packages", "woko2.actions");
    MockServletContext mockServletContext = new MockServletContext("basicroundtrip");
    mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
    mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
    mockServletContext.setAttribute Woko.CTX_KEY, new DummyURS().init()
    return mockServletContext;
  }

  void testSave() {
    def c = createMockServletContext()
    MockRoundtrip t = new MockRoundtrip(c, '/save/Book')
    def newObj = [nbPages:123,pocket:false,name:"Moby Dick",author:[name:"Melville"],_id:1,_type:'Book']
    t.addParameter('object._id', '1')
    t.addParameter('object._type', 'Book')
    t.addParameter('object.nbPages', '123')
    t.addParameter('object.pocket', 'false')
    t.addParameter('object.name', 'Moby Dick')
    t.addParameter('object.author.name', 'Melville')
    t.execute()    
    WokoActionBean ab = t.getActionBean(WokoActionBean.class)
    assert ab
    assert ab.object == newObj
    assert ab.facet.getClass() == Save.class
  }


}
