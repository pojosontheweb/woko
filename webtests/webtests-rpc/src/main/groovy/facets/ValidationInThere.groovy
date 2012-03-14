package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.StreamingResolution
import net.sourceforge.stripes.validation.Validate

@FacetKey(name = "validationInThere", profileId = "all")
class ValidationInThere extends BaseResolutionFacet {

    @Validate(required=true)
    String myProp

    Resolution getResolution(ActionBeanContext abc) {
        new StreamingResolution("text/plain", "OK : $myProp")
    }



}
