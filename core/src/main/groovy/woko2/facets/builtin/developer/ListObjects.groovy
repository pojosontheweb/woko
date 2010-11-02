package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import woko2.persistence.ResultIterator
import woko2.persistence.ListResultIterator
import woko2.facets.BaseForwardResolutionFacet

@FacetKey(name='list', profileId='developer')
class ListObjects extends BaseForwardResolutionFacet {

  Integer start = 0
  Integer limit = 10
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
    if (className==null) {
      resultIterator = new ListResultIterator([], start==null ? 0 : start, limit==null ? -1 : limit, 0)
    } else {
      resultIterator = context.woko.objectStore.list(className, start, limit)
    }
    return super.getResolution(abc)
  }

  


}
