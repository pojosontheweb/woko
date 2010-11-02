package facets

import test.OtherPojo
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.builtin.RenderPropertyValueJson
import woko2.facets.BaseFacet
import javax.servlet.http.HttpServletRequest
import org.json.JSONObject

@FacetKey(name='renderPropertyValueJson', profileId='all', targetObjectType=OtherPojo.class)
class RenderPropertyValueJsonOtherPojo extends BaseFacet implements RenderPropertyValueJson {

  def Object propertyToJson(HttpServletRequest request) {
    OtherPojo op = context.targetObject
    JSONObject res = new JSONObject()
    res.put('name', op.foo)
  }


}
