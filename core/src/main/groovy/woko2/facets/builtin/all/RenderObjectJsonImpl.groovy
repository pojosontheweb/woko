package woko2.facets.builtin.all

import woko2.facets.builtin.RenderObjectJson
import woko2.facets.BaseFacet
import net.sourceforge.jfacets.annotations.FacetKey
import org.json.JSONObject
import javax.servlet.http.HttpServletRequest
import woko2.facets.builtin.RenderProperties
import woko2.facets.builtin.RenderPropertyValueJson
import woko2.util.WLogger
import woko2.facets.builtin.RenderTitle

@FacetKey(name='renderObjectJson', profileId='all')
class RenderObjectJsonImpl extends BaseFacet implements RenderObjectJson {

  private static final WLogger logger = WLogger.getLogger(RenderObjectJsonImpl.class)

  def JSONObject objectToJson(HttpServletRequest request) {
    def o = context.targetObject
    if (o==null) {
      logger.debug("Object is null, returning null")
      return null
    }
    JSONObject result = new JSONObject()
    // find props to be rendered using renderProperties facet
    RenderProperties renderProperties = (RenderProperties)context.woko.getFacet(RenderProperties.name, request, o)
    if (renderProperties==null) {
      logger.warn("No renderProperties facet found for targetObject $o, as a result no props will be serialized.")
      return result
    }
    def propNamesAndValues = renderProperties.getPropertyValues()
    propNamesAndValues.each { k,v ->
      def jsonValue = propertyToJson(request, v)
      logger.debug("Converted prop $k to json $jsonValue")
      result.put(k, jsonValue)
    }

    def os = context.woko.objectStore
    String className = os.getClassMapping(o.getClass())
    result.put("_object", true)
    result.put("_className", className)
    String key = os.getKey(o)
    if (key) {
      result.put("_persistent", true)
      result.put("_key", key)
    }

    RenderTitle renderTitle = (RenderTitle)context.woko.getFacet(RenderTitle.name, request, o)
    if (renderTitle!=null) {
      result.put("_title", renderTitle.getTitle())
    }
    return result
  }

  def Object propertyToJson(HttpServletRequest request, Object value) {
    if (value==null) {
      return null
    }
    RenderPropertyValueJson rpvj = (RenderPropertyValueJson)context.woko.getFacet(RenderPropertyValueJson.name, request, value)
    if (rpvj==null) {
      // default to toString()
      return value.toString()
    }
    return rpvj.propertyToJson(request)
  }

}
