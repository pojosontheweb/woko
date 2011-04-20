package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import woko.facets.BaseResolutionFacet;

@FacetKey(name="toString", profileId="developer")
public class ToString extends BaseResolutionFacet {

  public Resolution getResolution(ActionBeanContext abc) {
    Object o = getFacetContext().getTargetObject();
    return new StreamingResolution("text/plain", o!=null ? o.toString() : "null");
  }

}
