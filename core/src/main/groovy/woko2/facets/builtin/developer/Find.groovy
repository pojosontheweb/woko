package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseForwardResolutionFacet
import woko2.persistence.ObjectStore

@FacetKey(name='find', profileId='developer')
class Find extends BaseForwardResolutionFacet {

  def getMappedClasses() {
    def res = []
    ObjectStore os = context.woko.objectStore
    def mappedClasses = os.getMappedClasses()
    mappedClasses.each { c ->
      res << os.getClassMapping(c)  
    }
    return res
  }


}