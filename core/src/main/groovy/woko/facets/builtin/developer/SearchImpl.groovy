package woko.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko.persistence.ResultIterator
import net.sourceforge.stripes.action.ActionBeanContext
import woko.facets.builtin.Search
import woko.persistence.ListResultIterator

@FacetKey(name='search', profileId='developer')
class SearchImpl extends BaseResultFacet implements Search {

  String query

  protected ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit) {
    if (query==null) {
      return new ListResultIterator([], start, limit, 0)
    } else {
      return facetContext.woko.objectStore.search(query, start, limit)
    }
  }

}                          
