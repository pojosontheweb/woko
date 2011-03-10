package woko.facets;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;

public interface ResolutionFacet {

  Resolution getResolution(ActionBeanContext abc);

}