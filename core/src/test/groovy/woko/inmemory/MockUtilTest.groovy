package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip
import net.sourceforge.stripes.mock.MockServletContext
import woko.Woko
import woko.mock.MockUtil
import woko.actions.WokoActionBean
import woko.facets.builtin.developer.ViewImpl

class MockUtilTest extends GroovyTestCase {

    void testMockUtil() {
        Woko woko = InMemRoundtripTestBase.createWoko("wdevel")
        new MockUtil().withServletContext(woko, { MockServletContext ctx ->
            MockRoundtrip trip = MockUtil.mockRoundtrip(ctx, "/view/woko.inmemory.Book/1", null)
            WokoActionBean wab = trip.getActionBean(WokoActionBean.class)
            assert wab.getClass() == WokoActionBean.class
            assert wab.facet.getClass() == ViewImpl.class
            assert wab.object.getClass() == Book.class
        } as MockUtil.Callback);
    }

}
