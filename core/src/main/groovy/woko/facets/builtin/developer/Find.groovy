package woko.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseForwardResolutionFacet
import woko.persistence.ObjectStore

@FacetKey(name='find', profileId='developer')
class Find extends BaseForwardResolutionFacet {

  String getPath() {
    return '/WEB-INF/woko/jsp/developer/find.jsp'
  }

  def getMappedClasses() {
    def res = []
    ObjectStore os = facetContext.woko.objectStore
    def mappedClasses = os.getMappedClasses()
    mappedClasses.each { c ->
      res << os.getClassMapping(c)  
    }
    return res
  }


}