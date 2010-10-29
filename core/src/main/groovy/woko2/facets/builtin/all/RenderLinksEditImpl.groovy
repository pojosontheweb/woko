package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.Woko
import woko2.facets.BaseFragmentFacet
import woko2.facets.FacetConstants
import javax.servlet.http.HttpServletRequest

@FacetKey(name='renderLinksEdit', profileId='all')
class RenderLinksEditImpl extends BaseFragmentFacet implements RenderLinks {

  def getLinks() {
    def links = []
    Woko woko = context.woko
    def o = context.targetObject
    def oc = context.targetObject.getClass()

    // display view link if object can be displayed
    def viewFacet = woko.getFacet(FacetConstants.view, request, o, oc)
    if (viewFacet) {
      String className = woko.objectStore.getClassMapping(oc)
      String key = woko.objectStore.getKey(o)
      links << [href:"view/$className/$key",text:'close editing']
    }

    def deleteFacet = woko.getFacet(FacetConstants.delete, request, o, oc)
    if (deleteFacet) {
      String className = woko.objectStore.getClassMapping(oc)
      String key = woko.objectStore.getKey(o)
      links << [href:"delete/$className/$key",text:'delete']
    }
    return links
  }

}
