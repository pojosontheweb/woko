package woko.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey

import woko.facets.builtin.Edit
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.stripes.action.ActionBeanContext
import net.sourceforge.stripes.action.Resolution
import woko.facets.BaseFacet

@FacetKey(name='edit', profileId='developer')
class EditImpl extends BaseFacet implements Edit {

  EditImpl() {
    acceptNullTargetObject = false
  }

  def String getFragmentPath() {
    return '/WEB-INF/woko/jsp/developer/edit.jsp'
  }

  def Resolution getResolution(ActionBeanContext abc) {
    return new ForwardResolution(fragmentPath)
  }



}
