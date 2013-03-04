package facets

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.Resolution
import woko.facets.BaseResolutionFacet

@FacetKey(name="testThrows", profileId = "developer")
class TestThrows extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        throw new RuntimeException("boom")
    }
}
