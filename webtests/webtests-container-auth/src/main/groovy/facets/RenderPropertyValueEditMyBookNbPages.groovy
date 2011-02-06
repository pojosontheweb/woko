package facets

import javax.servlet.http.HttpServletRequest
import net.sourceforge.jfacets.annotations.FacetKey

import woko.facets.BaseFragmentFacet
import woko.facets.builtin.RenderPropertyValueEdit

@FacetKey(name='renderPropertyValueEdit_nbPages', profileId='all', targetObjectType=test.MyBook.class)
class RenderPropertyValueEditMyBookNbPages extends BaseFragmentFacet implements RenderPropertyValueEdit {

  Object owningObject
  String propertyName
  Object propertyValue

  String getFragmentPath(HttpServletRequest request) {
    return '/WEB-INF/jsp/renderPropertyValueEditMyBookNbPages.jsp'
  }


}
