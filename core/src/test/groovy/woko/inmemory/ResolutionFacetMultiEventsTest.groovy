package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip
import net.sourceforge.stripes.mock.MockServletContext

class ResolutionFacetMultiEventsTest extends InMemRoundtripTestBase {

    void testGetResolutionIsCalledWhenNoRequestParamsAreSent() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            MockRoundtrip t = mockRoundtrip(ctx, "testMultiEvents", null, null, [:])
            assert t.outputString=="getResolution"
        }
    }

    void testAlternateEventIsCalled() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            MockRoundtrip t = mockRoundtrip(ctx, "testMultiEvents", null, null, ["otherEvent":"true"])
            assert t.outputString=="otherEvent"
        }
    }

    void testExceptionIsThrownWhenMoreThanOneHandlerMatches() {
        doWithMockContext("wdevel") { MockServletContext ctx ->
            try {
                mockRoundtrip("wdevel", "testMultiEvents", null, null, [
                        "otherEvent":"true",
                        "otherEvent2":"true"
                ])
                fail("Should have failed !")
            } catch(Exception e) {
                // normal behavior
            }
        }
    }

}
