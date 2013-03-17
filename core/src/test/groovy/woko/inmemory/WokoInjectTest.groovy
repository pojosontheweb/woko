package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip
import net.sourceforge.stripes.mock.MockServletContext

import static woko.mock.MockUtil.mockRoundtrip

class WokoInjectTest extends InMemRoundtripTestBase {

    void testInject() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            MockRoundtrip t = mockRoundtrip(ctx, "testInject", null, null, [:])
            assert t.outputString=="injected"
        }
    }

    void testInjectNoComponent() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            MockRoundtrip t = mockRoundtrip(ctx, "testInjectNoComponent", null, null, [:])
            assert t.outputString=="FAILED"
        }
    }

    void testInjectInvalidParameters() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            MockRoundtrip t = mockRoundtrip(ctx, "testInjectNoParams", null, null, [:])
            assert t.outputString=="FAILED"

            MockRoundtrip t2 = mockRoundtrip(ctx, "testInjectTooManyParams", null, null, [:])
            assert t2.outputString=="FAILED"
        }
    }

}
