package woko.facets

import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.action.ActionBeanContext

abstract class BaseForwardResolutionFacet extends BaseResolutionFacet {
  
  abstract String getPath()

  def Resolution getResolution(ActionBeanContext abc) {
    return new ForwardResolution(path)
  }


}
