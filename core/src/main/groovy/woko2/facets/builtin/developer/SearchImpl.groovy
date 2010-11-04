package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.persistence.ResultIterator
import net.sourceforge.stripes.action.ActionBeanContext
import woko2.facets.builtin.Search
import woko2.persistence.ListResultIterator

@FacetKey(name='search', profileId='developer')
class SearchImpl extends BaseResultFacet implements Search {

  String query

  protected ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit) {
    if (query==null) {
      return new ListResultIterator([], start, limit, 0)
    } else {
      return context.woko.objectStore.search(query, start, limit)
    }
  }

}                          
