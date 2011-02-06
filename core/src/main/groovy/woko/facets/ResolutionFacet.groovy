package woko.facets

import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext

public interface ResolutionFacet {

  Resolution getResolution(ActionBeanContext abc)

}