package facets

import net.sourceforge.jfacets.annotations.FacetKey

import woko2.facets.builtin.RenderPropertyValueJson
import woko2.facets.BaseFacet
import javax.servlet.http.HttpServletRequest
import org.json.JSONObject

@FacetKey(name='renderPropertyValueJson_nbPages', profileId='all', targetObjectType=test.MyBook.class)
class RenderPropertyValueJsonMyBookNbPages extends BaseFacet implements RenderPropertyValueJson {

  Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    JSONObject o = new JSONObject()
    Integer val = propertyValue
    o.put('nbPagesXXX', val)
    return o
  }


}
