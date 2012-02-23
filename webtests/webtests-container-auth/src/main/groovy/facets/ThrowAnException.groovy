package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext

@FacetKey(name="throw", profileId="all")
class ThrowAnException extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        throw new UnsupportedOperationException("foo, bar and baaaaaz !")
    }


}
