package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseForwardResolutionFacet
import woko2.facets.builtin.Edit

@FacetKey(name='edit', profileId='developer')
class EditImpl extends BaseForwardResolutionFacet implements Edit {

  EditImpl() {
    acceptNullTargetObject = false
  }


}
