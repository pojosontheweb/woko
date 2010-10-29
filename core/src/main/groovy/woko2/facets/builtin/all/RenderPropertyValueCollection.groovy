package woko2.facets.builtin.all

import javax.servlet.http.HttpServletRequest
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name='renderPropertyValue', profileId='all', targetObjectType=Collection.class)
class RenderPropertyValueCollection extends RenderPropertyValueImpl {

  def String getFragmentPath(HttpServletRequest request) {
    return '/WEB-INF/woko/jsp/all/renderPropertyValueCollection.jsp'
  }


}
