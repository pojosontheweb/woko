package woko2.facets.builtin.all

import woko2.Woko
import woko2.facets.BaseFragmentFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.FacetConstants

@FacetKey(name='renderTitle', profileId='all')
class RenderLinksImpl extends BaseFragmentFacet implements RenderLinks {

  def getLinks() {
    def links = []
    Woko woko = context.woko
    def o = context.targetObject
    def oc = context.targetObject.getClass()

    // display edit link if object can be edited
    def editFacet = woko.getFacet(FacetConstants.edit, request, o, oc)
    if (editFacet) {
      String className = woko.objectStore.getClassName(o)
      String key = woko.objectStore.getKey(o)
      links << [href:"edit/$className/$key",text:'edit']
    }

    def deleteFacet = woko.getFacet(FacetConstants.delete, request, o, oc)
    if (deleteFacet) {
      String className = woko.objectStore.getClassName(o)
      String key = woko.objectStore.getKey(o)
      links << [href:"delete/$className/$key",text:'delete']
    }
    return links
  }

}
