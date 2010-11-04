package woko2.facets.builtin.developer

import woko2.facets.BaseForwardResolutionFacet
import woko2.persistence.ResultIterator
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import woko2.facets.builtin.ResultFacet

abstract class BaseResultFacet extends BaseForwardResolutionFacet implements ResultFacet {

  Integer resultsPerPage = 10
  Integer page = 1
  
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
    page = page==null ? 1 : page
    resultsPerPage = resultsPerPage==null ? 10 : resultsPerPage
    int start = (page-1) * resultsPerPage
    int limit = resultsPerPage
    resultIterator = createResultIterator(abc, start, limit)
    return super.getResolution(abc)
  }

  protected abstract ResultIterator createResultIterator(ActionBeanContext abc, int start, int limit)

}
