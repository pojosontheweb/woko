package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseForwardResolutionFacet
import woko2.facets.builtin.View

@FacetKey(name='view', profileId='developer')
class ViewImpl extends BaseForwardResolutionFacet implements View {

  ViewImpl() {
    acceptNullTargetObject = false
  }
}
