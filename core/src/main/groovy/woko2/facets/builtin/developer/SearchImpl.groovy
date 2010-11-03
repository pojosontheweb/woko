package woko2.facets.builtin.developer

import woko2.facets.BaseForwardResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.persistence.ResultIterator
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import woko2.persistence.ListResultIterator

@FacetKey(name='search', profileId='developer')
class SearchImpl extends BaseForwardResolutionFacet {

  String query

  private ResultIterator resultIterator

  ResultIterator getResults() {
    return resultIterator
  }

  

  def Resolution getResolution(ActionBeanContext abc) {
    className = abc.request.getParameter('className')
    p = p==null ? 1 : p
    resultsPerPage = resultsPerPage==null ? 10 : resultsPerPage
    int start = (p-1) * resultsPerPage
    int limit = resultsPerPage

    if (className==null) {
      resultIterator = new ListResultIterator([], start, limit, 0)
    } else {
      resultIterator = context.woko.objectStore.list(className, start, limit)
    }
    return super.getResolution(abc)
  }


}
