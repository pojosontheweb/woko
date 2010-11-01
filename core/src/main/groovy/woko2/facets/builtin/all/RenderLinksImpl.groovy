package woko2.facets.builtin.all

import woko2.Woko
import woko2.facets.BaseFragmentFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.builtin.Edit
import woko2.facets.builtin.Delete
import woko2.facets.builtin.RenderLinks

@FacetKey(name='renderLinks', profileId='all')
class RenderLinksImpl extends BaseFragmentFacet implements RenderLinks {

  def getLinks() {
    def links = []
    Woko woko = context.woko
    def o = context.targetObject
    def oc = context.targetObject.getClass()

    // display edit link if object can be edited
    def editFacet = woko.getFacet(Edit.name, request, o, oc)
    if (editFacet) {
      String className = woko.objectStore.getClassMapping(oc)
      String key = woko.objectStore.getKey(o)
      links << [href:"edit/$className/$key",text:'edit']
    }

    def deleteFacet = woko.getFacet(Delete.name, request, o, oc)
    if (deleteFacet) {
      String className = woko.objectStore.getClassMapping(oc)
      String key = woko.objectStore.getKey(o)
      links << [href:"delete/$className/$key",text:'delete']
    }
    return links
  }

}
