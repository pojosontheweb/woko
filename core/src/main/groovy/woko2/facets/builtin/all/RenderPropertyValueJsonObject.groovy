package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseFacet
import woko2.facets.builtin.RenderPropertyValueJson
import javax.servlet.http.HttpServletRequest
import org.json.JSONObject
import woko2.facets.builtin.RenderTitle

@FacetKey(name='renderPropertyValueJson', profileId='all')
class RenderPropertyValueJsonObject extends BaseFacet implements RenderPropertyValueJson {

  Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    if (propertyValue==null) {
      return null
    }
    JSONObject res = new JSONObject()
    res.put('_object', true)
    Class c = propertyValue.getClass()
    def os = context.woko.objectStore
    String className = os.getClassMapping(c)
    res.put('_className', className)
    String key = os.getKey(propertyValue)
    if (key) {
      res.put('_persistent', true)
      res.put('_key', key)
    }

    RenderTitle renderTitle = (RenderTitle)context.woko.getFacet(RenderTitle.name, request, propertyValue)
    if (renderTitle!=null) {
      res.put("_title", renderTitle.getTitle())
    }

    return res
  }

}