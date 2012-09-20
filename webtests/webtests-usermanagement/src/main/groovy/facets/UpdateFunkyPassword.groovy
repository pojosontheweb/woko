package facets

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.StreamingResolution

@FacetKey(name="updateFunkyPassword",profileId="developer")
class UpdateFunkyPassword extends BaseResolutionFacet {

    Resolution getResolution(ActionBeanContext abc) {
        def um = woko.userManager
        def u = um.getUserByUsername("funkystuff")
        u.password = um.encodePassword("funkystuff")

        return new StreamingResolution("text/plain", "OK")
    }


}
