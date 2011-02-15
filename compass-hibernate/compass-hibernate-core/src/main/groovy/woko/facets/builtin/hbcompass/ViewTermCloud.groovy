package woko.facets.builtin.hbcompass

import woko.facets.BaseResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko.hbcompass.tagcloud.CompassCloud
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.ForwardResolution

@FacetKey(name = "termCloud", profileId = "developer")
class ViewTermCloud extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        return new ForwardResolution('/WEB-INF/woko/jsp/hbcompass/term-cloud.jsp')
    }

    CompassCloud getCloud() {
        return new CompassCloud()
    }


}
