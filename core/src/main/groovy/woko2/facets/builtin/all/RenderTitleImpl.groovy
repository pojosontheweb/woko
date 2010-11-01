package woko2.facets.builtin.all

import woko2.facets.BaseFragmentFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.builtin.RenderTitle

@FacetKey(name='renderTitle', profileId='all')
class RenderTitleImpl extends BaseFragmentFacet implements RenderTitle {
               
  String getTitle() {
    def o = context.targetObject
    if (o==null) {
      return 'null'
    }
    def propNames = ['title', 'name', 'id', '_id']
    if (o instanceof Map) {
      for (String s : propNames) {
        if (o.containsKey(s)) {
          return o[s]
        }
      }
    } else {
      for (String s : propNames) {
        try {
          return o[s]
        } catch(MissingPropertyException) {
          // do nothing
        }
      }
    }

    // nothing matched, compute a meaningful title
    def objectStore = context?.woko?.objectStore
    assert objectStore
    def className = objectStore.getClassMapping(o.getClass())
    def key = objectStore.getKey(o)
    if (className && key) {
      return "$key@$className"
    }
    return o.toString()
  }

}
