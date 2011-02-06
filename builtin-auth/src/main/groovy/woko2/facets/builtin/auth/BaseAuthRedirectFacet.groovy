package woko2.facets.builtin.auth

import woko2.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.RedirectResolution

class BaseAuthRedirectFacet extends BaseResolutionFacet {

  String targetUrl

  Resolution getResolution(ActionBeanContext abc) {
    return new RedirectResolution(targetUrl).addParameters(abc.getRequest().getParameterMap())
  }


}
