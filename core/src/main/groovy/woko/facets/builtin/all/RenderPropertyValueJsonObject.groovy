package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseFacet
import woko.facets.builtin.RenderPropertyValueJson
import javax.servlet.http.HttpServletRequest
import org.json.JSONObject
import woko.facets.builtin.RenderTitle

@FacetKey(name='renderPropertyValueJson', profileId='all')
class RenderPropertyValueJsonObject extends BaseFacet implements RenderPropertyValueJson {

  Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    if (propertyValue==null) {
      return null
    }
    JSONObject res = new JSONObject()
    res.put('_object', true)
    Class c = propertyValue.getClass()
    def os = facetContext.woko.objectStore
    String className = os.getClassMapping(c)
    res.put('_className', className)
    String key = os.getKey(propertyValue)
    if (key) {
      res.put('_persistent', true)
      res.put('_key', key)
    }

    RenderTitle renderTitle = (RenderTitle)facetContext.woko.getFacet('renderTitle', request, propertyValue)
    if (renderTitle!=null) {
      res.put("_title", renderTitle.getTitle())
    }

    return res
  }

}
