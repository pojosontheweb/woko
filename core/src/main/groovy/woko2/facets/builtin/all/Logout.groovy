package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.FacetConstants
import woko2.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.SimpleMessage

@FacetKey(name='logout', profileId='all')
class Logout extends BaseResolutionFacet {

  Resolution getResolution(ActionBeanContext abc) {
    abc.request.session.invalidate()
    abc.messages.add(new SimpleMessage("You have been logged out."))
    return new RedirectResolution("/${FacetConstants.home}")
  }

}
