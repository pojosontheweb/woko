package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.validation.Validate
import net.sourceforge.stripes.action.DontValidate
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name="multiEventsWithDontValidate",profileId="all")
class MultiEventsWithDontValidate extends BaseResolutionFacet {

    @Validate(required=true)
    String myProp

    private Resolution doIt() {
        return new StreamingResolution("text/plain", "myProp:$myProp")
    }

    @DontValidate
    Resolution getResolution(ActionBeanContext abc) {
        doIt()
    }

    Resolution otherEvent(ActionBeanContext abc) {
        doIt()
    }


}
