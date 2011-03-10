package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.SimpleMessage
import woko.facets.builtin.Logout

@FacetKey(name='logout', profileId='all')
class LogoutImpl extends BaseResolutionFacet implements Logout {

  Resolution getResolution(ActionBeanContext abc) {
    abc.request.session.invalidate()
    abc.messages.add(new SimpleMessage("You have been logged out."))
    return new RedirectResolution("/home")
  }

}
