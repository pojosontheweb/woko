package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko.Woko
import woko.facets.BaseFragmentFacet
import woko.facets.builtin.View
import woko.facets.builtin.Delete
import woko.facets.builtin.RenderLinks

@FacetKey(name='renderLinksEdit', profileId='all')
class RenderLinksEditImpl extends BaseFragmentFacet implements RenderLinks {

  String getPath() {
    return '/WEB-INF/woko/jsp/all/renderLinksEdit.jsp'
  }
  
  def getLinks() {
    def links = []
    Woko woko = facetContext.woko
    def o = facetContext.targetObject
    def oc = facetContext.targetObject.getClass()

    // display view link if object can be displayed
    def viewFacet = woko.getFacet('view', request, o, oc)
    if (viewFacet) {
      String className = woko.objectStore.getClassMapping(oc)
      String key = woko.objectStore.getKey(o)
      links << [href:"view/$className/$key",text:'Close editing']
    }

    def deleteFacet = woko.getFacet('delete', request, o, oc)
    if (deleteFacet) {
      String className = woko.objectStore.getClassMapping(oc)
      String key = woko.objectStore.getKey(o)
      links << [href:"delete/$className/$key",text:'Delete']
    }
    return links
  }

}
