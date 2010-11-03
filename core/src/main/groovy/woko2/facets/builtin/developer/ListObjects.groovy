package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import woko2.persistence.ResultIterator
import woko2.persistence.ListResultIterator
import woko2.facets.BaseForwardResolutionFacet

@FacetKey(name='list', profileId='developer')
class ListObjects extends BaseForwardResolutionFacet {

  Integer resultsPerPage = 10
  Integer p = 1
  private String className

  private ResultIterator resultIterator

  ResultIterator getResults() {
    return resultIterator
  }

  String getClassName() {
    return className
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
