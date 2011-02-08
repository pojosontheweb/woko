package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import javax.servlet.http.HttpServletRequest

@FacetKey(name='renderPropertyValue', profileId='all', targetObjectType=Date.class)
class RenderPropertyValueDate extends RenderPropertyValueImpl {

  String getPath() {
    return '/WEB-INF/woko/jsp/all/renderPropertyValueDate.jsp'
  }


}
