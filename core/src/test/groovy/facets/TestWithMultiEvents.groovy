package facets

import woko.facets.BaseResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name="testMultiEvents",profileId="developer")
class TestWithMultiEvents extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        return new StreamingResolution("text/plain", "getResolution")
    }

    Resolution otherEvent() {
        return new StreamingResolution("text/plain", "otherEvent")
    }

    Resolution otherEvent2() {
        return new StreamingResolution("text/plain", "otherEvent2")
    }

}
