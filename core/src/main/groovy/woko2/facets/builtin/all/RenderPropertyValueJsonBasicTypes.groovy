package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.builtin.RenderPropertyValueJson
import woko2.facets.BaseFacet
import javax.servlet.http.HttpServletRequest
import net.sourceforge.jfacets.annotations.FacetKeyList

@FacetKeyList(
keys=[
  @FacetKey(name='renderPropertyValueJson', profileId='all', targetObjectType=Number.class),
  @FacetKey(name='renderPropertyValueJson', profileId='all', targetObjectType=Date.class),
  @FacetKey(name='renderPropertyValueJson', profileId='all', targetObjectType=String.class),
  @FacetKey(name='renderPropertyValueJson', profileId='all', targetObjectType=Boolean.class)
])
class RenderPropertyValueJsonBasicTypes extends BaseFacet implements RenderPropertyValueJson {

  Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    // catch-all : return the target object itself
    return propertyValue
  }


}
