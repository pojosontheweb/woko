package woko2.facets.builtin.containerauth

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.SimpleMessage
import woko2.facets.builtin.Login

@FacetKey(name='login', profileId='all')
class LoginImpl extends BaseResolutionFacet implements Login {

  def Resolution getResolution(ActionBeanContext abc) {
    abc.messages.add(new SimpleMessage("You have been logged in"))
    return new RedirectResolution('/home')
  }


}
