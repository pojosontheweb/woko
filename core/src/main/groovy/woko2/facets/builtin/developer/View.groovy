package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseForwardResolutionFacet

@FacetKey(name='view', profileId='developer')
class View extends BaseForwardResolutionFacet {

  View() {
    acceptNullTargetObject = false
  }
}
