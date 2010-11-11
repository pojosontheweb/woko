package facets

import net.sourceforge.jfacets.annotations.FacetKey

import woko2.facets.builtin.RenderPropertyValue
import woko2.facets.BaseFragmentFacet
import javax.servlet.http.HttpServletRequest

@FacetKey(name='renderPropertyValue_nbPages', profileId='all', targetObjectType=test.MyBook.class)
class RenderPropertyValueMyBookNbPages extends BaseFragmentFacet implements RenderPropertyValue {

  Object owningObject
  String propertyName
  Object propertyValue

  String getFragmentPath(HttpServletRequest request) {
    return '/WEB-INF/jsp/renderPropertyValueMyBookNbPages.jsp'
  }


}
