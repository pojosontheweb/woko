package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import testentity.MyValidatedPojo
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name = "testObjectValidate", profileId = "all", targetObjectType = MyValidatedPojo.class)
class TestWithObjectValidation extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        MyValidatedPojo p = facetContext.targetObject
        return new StreamingResolution("text/plain", "value:$p.str")
    }


}
