package woko2.facets.builtin.developer

import woko2.facets.BaseForwardResolutionFacet
import woko2.persistence.ResultIterator
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext

abstract class BaseResultFacet extends BaseForwardResolutionFacet {

  Integer resultsPerPage = 10
  Integer p = 1
  String className

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
    resultIterator = createResultIterator(abc, start, limit)
    return super.getResolution(abc)
  }

  protected abstract ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit)

}
