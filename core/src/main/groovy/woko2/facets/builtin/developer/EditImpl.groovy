package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseForwardResolutionFacet
import woko2.facets.builtin.Edit
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.Resolution
import woko2.facets.BaseFacet

@FacetKey(name='edit', profileId='developer')
class EditImpl extends BaseFacet implements Edit {

  EditImpl() {
    acceptNullTargetObject = false
  }

  def String getFragmentPath() {
    return computeJspPathFromFacetDescriptor()
  }

  def Resolution getResolution(ActionBeanContext abc) {
    return new ForwardResolution(fragmentPath)
  }



}
