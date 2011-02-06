package woko.facets.builtin.all

import woko.facets.BaseFragmentFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.RenderTitle

@FacetKey(name='renderTitle', profileId='all')
class RenderTitleImpl extends BaseFragmentFacet implements RenderTitle {
               
  String getTitle() {
    def o = facetContext.targetObject
    if (o==null) {
      return 'null'
    }
    def result = null
    def propNames = ['title', 'name', 'id', '_id']
    if (o instanceof Map) {
      for (String s : propNames) {
        if (o.containsKey(s)) {
          result = o[s]
          break
        }
      }
    } else {
      for (String s : propNames) {
        try {
          result = o[s]
          break
        } catch(MissingPropertyException) {
          // do nothing
        }
      }
    }

    if (result) {
      return result
    }

    // nothing matched, compute a meaningful title
    def objectStore = facetContext?.woko?.objectStore
    assert objectStore
    def className = objectStore.getClassMapping(o.getClass())
    def key = objectStore.getKey(o)
    if (className && key) {
      return "$key@$className"
    }
    return o.toString()    
  }

}
