package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import javax.servlet.http.HttpServletRequest

@FacetKey(name='renderPropertyValueEdit', profileId='all', targetObjectType=String.class)
class RenderPropertyValueEditString extends RenderPropertyValueImpl {

  def String getFragmentPath(HttpServletRequest request) {
    super.getFragmentPath(request)
    return '/WEB-INF/woko/jsp/all/renderPropertyValueEditString.jsp'
  }


}
