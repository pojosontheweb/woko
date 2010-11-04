package facets

import net.sourceforge.jfacets.annotations.FacetKey
import test.OtherPojo
import woko2.facets.builtin.RenderPropertyValue
import woko2.facets.BaseFragmentFacet
import javax.servlet.http.HttpServletRequest

@FacetKey(name='renderPropertyValue', profileId='all', targetObjectType=OtherPojo.class)
class RenderPropertyValueOtherPojo extends BaseFragmentFacet implements RenderPropertyValue {

  Object owningObject
  String propertyName
  Object propertyValue

  String getFragmentPath(HttpServletRequest request) {
    return '/WEB-INF/jsp/renderPropertyValueOtherPojo.jsp' 
  }


}
