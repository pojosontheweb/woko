package woko.inmemory

import facets.TestWithRoute
import net.sourceforge.stripes.mock.MockServletContext
import woko.actions.WokoActionBean

class RoutesRoundtripTest extends InMemRoundtripTestBase {

    void testSimpleRoute() {

        def doTest = { MockServletContext ctx, String url ->
            WokoActionBean wokoActionBean =
                mockRoundtrip(ctx, url, null).getActionBean(WokoActionBean.class)
            assert wokoActionBean.facet.getClass() == TestWithRoute.class
        }

        doWithMockContext("wdevel") { MockServletContext ctx ->
            // test regular URL
            doTest(ctx, "/testWithRoute/woko.inmemory.Book/1")

            // test simple route
//            doTest(ctx, "/route/to/book/1")
        }
    }

}
