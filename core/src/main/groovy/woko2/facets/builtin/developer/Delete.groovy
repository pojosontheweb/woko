package woko2.facets.builtin.developer

import net.sourceforge.stripes.action.RedirectResolution
import net.sourceforge.stripes.action.SimpleMessage
import net.sourceforge.stripes.action.ForwardResolution
import woko2.util.Util
import woko2.facets.BaseResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.FacetConstants
import woko2.Woko
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext

@FacetKey(name='delete', profileId='developer')
class Delete extends BaseResolutionFacet {

  Resolution getResolution(ActionBeanContext abc) {
    def request = abc.request
    if (request.getParameter('cancel')) {
      abc.messages.add(new SimpleMessage('Cancelled deletion'))
      return new RedirectResolution(
              Util.facetUrl(
                      context.woko,
                      FacetConstants.view ,
                      context.targetObject))
    }
    if (request.getParameter('delete')) {
      context.woko.objectStore.delete(context.targetObject)
      abc.messages.add(new SimpleMessage('Object deleted'))
      return new RedirectResolution("/${FacetConstants.home}")
    }
    // not confirmed, we display the confirm screen
    return new ForwardResolution('/WEB-INF/jsp/developer/confirmDelete.jsp')
  }

}
