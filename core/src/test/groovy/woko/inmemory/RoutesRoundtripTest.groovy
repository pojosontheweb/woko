package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip
import net.sourceforge.stripes.mock.MockServletContext

class RoutesRoundtripTest extends InMemRoundtripTestBase {

    /**
     * Simple route using an ActionBean
     * @see woko.actions.MyRouteActionBean
     */
    void testSimpleRoute() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            // test regular URL
            def defaultUrl = "/testWithRoute/woko.inmemory.Book/1"
            MockRoundtrip t = mockRoundtrip(ctx, defaultUrl, null)
            assert t.outputString == "FUNKY"

            // test simple route
            MockRoundtrip t2 = mockRoundtrip(ctx, "/route/to/book/1", null)
            t2.forwardUrl == defaultUrl
        }
    }

}
