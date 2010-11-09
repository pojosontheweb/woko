package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseFacet
import woko2.facets.builtin.RenderPropertyValueJson
import javax.servlet.http.HttpServletRequest

@FacetKey(name='renderPropertyValueJson', profileId='all', targetObjectType=Class.class)
class RenderPropertyValueJsonClass extends BaseFacet implements RenderPropertyValueJson {

  def Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    return facetContext.woko.objectStore.getClassMapping(propertyValue)
  }


}
