package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey

import net.sourceforge.stripes.action.ActionBeanContext
import woko2.persistence.ResultIterator
import woko2.persistence.ListResultIterator

@FacetKey(name='list', profileId='developer')
class ListImpl extends BaseResultFacet implements woko2.facets.builtin.ListObjects {

  protected ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit) {
    if (className==null) {
      return new ListResultIterator([], start, limit, 0)
    } else {
      return facetContext.woko.objectStore.list(className, start, limit)
    }
  }


}
