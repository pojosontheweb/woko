package woko.inmemory

import net.sourceforge.stripes.mock.MockRoundtrip

class ResolutionFacetMultiEventsTest extends InMemRoundtripTestBase {

    void testGetResolutionIsCalledWhenNoRequestParamsAreSent() {
        MockRoundtrip t = mockRoundtrip("wdevel", "testMultiEvents", null, null, [:])
        assert t.outputString=="getResolution"
    }

    void testAlternateEventIsCalled() {
        MockRoundtrip t = mockRoundtrip("wdevel", "testMultiEvents", null, null, ["otherEvent":"true"])
        assert t.outputString=="otherEvent"
    }

    void testExceptionIsThrownWhenMoreThanOneHandlerMatches() {
        try {
            MockRoundtrip t = mockRoundtrip("wdevel", "testMultiEvents", null, null, [
                    "otherEvent":"true",
                    "otherEvent2":"true"
            ])
            fail("Should have failed !")
        } catch(Exception e) {
            // normal behavior
        }
    }

}
