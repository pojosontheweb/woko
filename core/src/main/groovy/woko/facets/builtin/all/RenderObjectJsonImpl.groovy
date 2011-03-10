package woko.facets.builtin.all

import woko.facets.builtin.RenderObjectJson
import woko.facets.BaseFacet
import net.sourceforge.jfacets.annotations.FacetKey
import org.json.JSONObject
import javax.servlet.http.HttpServletRequest
import woko.facets.builtin.RenderProperties
import woko.facets.builtin.RenderPropertyValueJson
import woko.util.WLogger
import woko.facets.builtin.RenderTitle

@FacetKey(name='renderObjectJson', profileId='all')
class RenderObjectJsonImpl extends BaseFacet implements RenderObjectJson {

  private static final WLogger logger = WLogger.getLogger(RenderObjectJsonImpl.class)

  def JSONObject objectToJson(HttpServletRequest request) {
    def o = facetContext.targetObject
    if (o==null) {
      logger.debug("Object is null, returning null")
      return null
    }
    JSONObject result = new JSONObject()
    // find props to be rendered using renderProperties facet
    RenderProperties renderProperties = (RenderProperties)facetContext.woko.getFacet('renderProperties', request, o)
    if (renderProperties==null) {
      logger.warn("No renderProperties facet found for targetObject $o, as a result no props will be serialized.")
      return result
    }
    def propNamesAndValues = renderProperties.getPropertyValues()
    propNamesAndValues.each { k,v ->
      def jsonValue = propertyToJson(request, o, k, v)
      logger.debug("Converted prop $k to json $jsonValue")
      result.put(k, jsonValue)
    }

    def os = facetContext.woko.objectStore
    String className = os.getClassMapping(o.getClass())
    result.put("_object", true)
    result.put("_className", className)
    String key = os.getKey(o)
    if (key) {
      result.put("_persistent", true)
      result.put("_key", key)
    }

    RenderTitle renderTitle = (RenderTitle)facetContext.woko.getFacet('renderTitle', request, o)
    if (renderTitle!=null) {
      result.put("_title", renderTitle.getTitle())
    }
    return result
  }

  def Object propertyToJson(HttpServletRequest request, Object owner, String propertyName, Object value) {
    if (value==null) {
      return null
    }
    // try name-specific first
    RenderPropertyValueJson rpvj = (RenderPropertyValueJson)facetContext.woko.getFacet('renderPropertyValue' + "_$propertyName", request, owner)
    if (rpvj==null) {
      // type-specific
      rpvj = (RenderPropertyValueJson)facetContext.woko.getFacet('renderPropertyValue', request, value)
    }
    if (rpvj==null) {
      // default to toString()
      return value.toString()
    }
    return rpvj.propertyToJson(request, value)
  }

}
