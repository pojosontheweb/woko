package woko.facets.builtin.all

import woko.Woko
import woko.facets.BaseFragmentFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.Edit
import woko.facets.builtin.Delete
import woko.facets.builtin.RenderLinks

@FacetKey(name='renderLinks', profileId='all')
class RenderLinksImpl extends BaseFragmentFacet implements RenderLinks {

  String getPath() {
    return '/WEB-INF/woko/jsp/all/renderLinks.jsp'
  }

  def getLinks() {
    def links = []
    Woko woko = facetContext.woko
    def o = facetContext.targetObject
    def oc = facetContext.targetObject.getClass()

    // display edit link if object can be edited
    def editFacet = woko.getFacet('edit', request, o, oc)
    if (editFacet) {
      String className = woko.objectStore.getClassMapping(oc)
      String key = woko.objectStore.getKey(o)
      links << [href:"edit/$className/$key",text:'Edit']
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
