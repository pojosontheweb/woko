package woko.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey

import net.sourceforge.stripes.action.ActionBeanContext
import woko.persistence.ResultIterator
import woko.persistence.ListResultIterator

@FacetKey(name='list', profileId='developer')
class ListImpl extends BaseResultFacet implements woko.facets.builtin.ListObjects {

  String getPath() {
    return '/WEB-INF/woko/jsp/developer/list.jsp'
  }

  protected ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit) {
    if (className==null) {
      return new ListResultIterator([], start, limit, 0)
    } else {
      return facetContext.woko.objectStore.list(className, start, limit)
    }
  }


}
