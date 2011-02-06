package woko.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseForwardResolutionFacet
import woko.facets.builtin.View

@FacetKey(name='view', profileId='developer')
class ViewImpl extends BaseForwardResolutionFacet implements View {

  ViewImpl() {
    acceptNullTargetObject = false
  }
}
