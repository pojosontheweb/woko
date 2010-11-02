package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseFacet
import woko2.facets.builtin.RenderPropertyValueJson
import javax.servlet.http.HttpServletRequest
import org.json.JSONArray
import woko2.util.WLogger

@FacetKey(name='renderPropertyValueJson', profileId='all', targetObjectType=Collection.class)
class RenderPropertyValueJsonCollection extends BaseFacet implements RenderPropertyValueJson {

  private static final WLogger logger = WLogger.getLogger(RenderPropertyValueJsonCollection.class)

  Object propertyToJson(HttpServletRequest request) {
    def o = context.targetObject
    JSONArray arr = new JSONArray()
    o.each { item ->
      logger.debug("converting collection item $item to json...")
      if (item==null) {
        arr.put(null)
      }
      RenderPropertyValueJson rpvj = (RenderPropertyValueJson)context.woko.getFacet(RenderPropertyValueJson.name, request, item)
      if (rpvj==null) {
        logger.debug("... no renderPropertyValueJson facet found for collection item, adding null")
        arr.put(null)
      } else {
        def json = rpvj.propertyToJson(request)
        logger.debug("... converted item $item to $json")
        arr.put(json)
      }            
    }
    return arr
  }

}
