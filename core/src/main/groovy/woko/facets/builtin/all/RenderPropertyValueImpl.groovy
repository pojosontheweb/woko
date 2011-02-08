package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseFragmentFacet
import woko.facets.builtin.RenderPropertyValue

@FacetKey(name='renderPropertyValue', profileId='all')
class RenderPropertyValueImpl extends BaseFragmentFacet implements RenderPropertyValue {

  Object owningObject
  String propertyName
  Object propertyValue

  String getPath() {
    return '/WEB-INF/woko/jsp/all/renderPropertyValue.jsp'
  }


}
