package facets

import net.sourceforge.jfacets.annotations.FacetKey

import woko.facets.builtin.RenderPropertyValue
import woko.facets.BaseFragmentFacet
import javax.servlet.http.HttpServletRequest

@FacetKey(name='renderPropertyValue_nbPages', profileId='all', targetObjectType=test.MyBook.class)
class RenderPropertyValueMyBookNbPages extends BaseFragmentFacet implements RenderPropertyValue {

  Object owningObject
  String propertyName
  Object propertyValue

  String getPath() {
    return '/WEB-INF/jsp/renderPropertyValueMyBookNbPages.jsp'
  }


}
