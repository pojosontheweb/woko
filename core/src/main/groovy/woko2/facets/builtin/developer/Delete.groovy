package woko2.facets.builtin.developer

import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.SimpleMessage
import net.sourceforge.stripes.action.ForwardResolution
import woko2.facets.BaseResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.FacetConstants
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext

@FacetKey(name='delete', profileId='developer')
class Delete extends BaseResolutionFacet {

  boolean confirm = false
  boolean cancel = false

  Delete() {
    acceptNullTargetObject = false
  }

  Resolution getResolution(ActionBeanContext abc) {
    if (cancel) {
      abc.messages.add(new SimpleMessage('Cancelled deletion'))
      return new RedirectResolution(
              context.woko.facetUrl(
                      FacetConstants.view ,
                      context.targetObject))
    }
    if (confirm) {
      context.woko.objectStore.delete(context.targetObject)
      abc.messages.add(new SimpleMessage('Object deleted'))
      return new RedirectResolution("/${FacetConstants.home}")
    }
    // not confirmed, we display the confirm screen
    return new ForwardResolution('/WEB-INF/woko/jsp/developer/confirmDelete.jsp')
  }

}
