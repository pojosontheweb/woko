package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.Woko
import woko2.facets.BaseFragmentFacet
import woko2.facets.builtin.View
import woko2.facets.builtin.Delete
import woko2.facets.builtin.RenderLinks

@FacetKey(name='renderLinksEdit', profileId='all')
class RenderLinksEditImpl extends BaseFragmentFacet implements RenderLinks {

  def getLinks() {
    def links = []
    Woko woko = facetContext.woko
    def o = facetContext.targetObject
    def oc = facetContext.targetObject.getClass()

    // display view link if object can be displayed
    def viewFacet = woko.getFacet(View.name, request, o, oc)
    if (viewFacet) {
      String className = woko.objectStore.getClassMapping(oc)
      String key = woko.objectStore.getKey(o)
      links << [href:"view/$className/$key",text:'Close editing']
    }

    def deleteFacet = woko.getFacet(Delete.name, request, o, oc)
    if (deleteFacet) {
      String className = woko.objectStore.getClassMapping(oc)
      String key = woko.objectStore.getKey(o)
      links << [href:"delete/$className/$key",text:'Delete']
    }
    return links
  }

}
