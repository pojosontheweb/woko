package woko2.facets.builtin.developer

import net.sourceforge.stripes.action.StreamingResolution
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.Resolution
import woko2.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.ActionBeanContext

@FacetKey(name='toString', profileId='developer')
class ToString extends BaseResolutionFacet {

  Resolution getResolution(ActionBeanContext abc) {
    return new StreamingResolution('text/plain', facetContext?.targetObject?.toString()) 
  }

}
