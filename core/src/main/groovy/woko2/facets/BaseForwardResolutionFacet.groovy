package woko2.facets

import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.action.ActionBeanContext

abstract class BaseForwardResolutionFacet extends BaseResolutionFacet {
  
  String getPath() {
    return computeJspPathFromFacetDescriptor()
  }

  def Resolution getResolution(ActionBeanContext abc) {
    return new ForwardResolution(path)
  }


}
