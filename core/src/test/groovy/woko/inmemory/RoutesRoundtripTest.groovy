package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip
import woko.actions.WokoActionBean
import woko.facets.builtin.developer.ViewImpl

class RoutesRoundtripTest extends InMemRoundtripTestBase {

    void testNoRouteWithCleanBinding() {
        MockRoundtrip t = new MockRoundtrip(createMockServletContext("wdevel"), "/view/woko.inmemory.Book/1")
        t.execute()
        assert t.getActionBean(WokoActionBean.class).facet.getClass() == ViewImpl.class
    }

}
