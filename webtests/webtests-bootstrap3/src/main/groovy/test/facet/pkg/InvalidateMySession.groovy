package test.facet.pkg

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.action.Resolution
import woko.facets.BaseResolutionFacet

@FacetKey(name="invalidateMySession", profileId = "developer")
class InvalidateMySession extends BaseResolutionFacet {

    @Override
    Resolution getResolution(ActionBeanContext abc) {
        request.session.invalidate()
        return new ForwardResolution("/WEB-INF/jsp/invalidateMySession.jsp")
    }
}
