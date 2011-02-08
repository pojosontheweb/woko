package woko.facets.builtin.all

import javax.servlet.http.HttpServletRequest
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name='renderPropertyValue', profileId='all', targetObjectType=Collection.class)
class RenderPropertyValueCollection extends RenderPropertyValueImpl {

  String getPath() {
    return '/WEB-INF/woko/jsp/all/renderPropertyValueCollection.jsp'
  }


}
