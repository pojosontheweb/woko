package woko2.facets.builtin.developer

import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.SimpleMessage
import net.sourceforge.stripes.action.ForwardResolution
import woko2.facets.BaseResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import woko2.facets.builtin.Delete
import woko2.facets.builtin.View
import woko2.facets.builtin.Home

@FacetKey(name='delete', profileId='developer')
class DeleteImpl extends BaseResolutionFacet implements Delete {

  String confirm
  String cancel 

  DeleteImpl() {
    acceptNullTargetObject = false
  }

  Resolution getResolution(ActionBeanContext abc) {
    if (cancel) {
      abc.messages.add(new SimpleMessage('Cancelled deletion'))
      return new RedirectResolution(
              facetContext.woko.facetUrl(
                      View.name ,
                      facetContext.targetObject))
    }
    if (confirm) {
      facetContext.woko.objectStore.delete(facetContext.targetObject)
      abc.messages.add(new SimpleMessage('Object deleted'))
      return new RedirectResolution("/${Home.name}")
    }
    // not confirmed, we display the confirm screen
    return new ForwardResolution('/WEB-INF/woko/jsp/developer/confirmDelete.jsp')
  }

}
