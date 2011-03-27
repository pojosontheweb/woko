package woko.facets.builtin.all;


import net.sourceforge.jfacets.annotations.FacetKey;
import org.json.JSONException;
import org.json.JSONObject;
import woko.Woko;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderObjectJson;
import woko.facets.builtin.RenderProperties;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.RenderTitle;
import woko.persistence.ObjectStore;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@FacetKey(name="renderObjectJson", profileId="all")
public class RenderObjectJsonImpl extends BaseFacet implements RenderObjectJson {

  private static final WLogger logger = WLogger.getLogger(RenderObjectJsonImpl.class);

  public JSONObject objectToJson(HttpServletRequest request) {
    WokoFacetContext facetContext = getFacetContext();
    Object o = facetContext.getTargetObject();
    try {
      if (o==null) {
        logger.debug("Object is null, returning null");
        return null;
      }
      JSONObject result = new JSONObject();
      Woko woko = facetContext.getWoko();
      // find props to be rendered using renderProperties facet
      RenderProperties renderProperties = (RenderProperties)woko.getFacet("renderProperties", request, o);
      if (renderProperties==null) {
        logger.warn("No renderProperties facet found for targetObject $o, as a result no props will be serialized.");
        return result;
      }
      Map<String,Object> propNamesAndValues = renderProperties.getPropertyValues();
      for (String k : propNamesAndValues.keySet()) {
        Object v = propNamesAndValues.get(k);
        Object jsonValue = propertyToJson(request, o, k, v);
        logger.debug("Converted prop $k to json $jsonValue");
        result.put(k, jsonValue);
      }

      ObjectStore os = woko.getObjectStore();
      String className = os.getClassMapping(o.getClass());
      result.put("_object", true);
      result.put("_className", className);
      String key = os.getKey(o);
      if (key!=null) {
        result.put("_persistent", true);
        result.put("_key", key);
      }

      RenderTitle renderTitle = (RenderTitle)woko.getFacet("renderTitle", request, o);
      if (renderTitle!=null) {
        result.put("_title", renderTitle.getTitle());
      }
      return result;
    } catch(JSONException e) {
      logger.error("Unable to convert object to JSON : " + o);
      throw new RuntimeException(e);
    }
  }

  Object propertyToJson(HttpServletRequest request, Object owner, String propertyName, Object value) {
    if (value==null) {
      return null;
    }
    WokoFacetContext facetContext = getFacetContext();
    Woko woko = facetContext.getWoko();
    // try name-specific first
    RenderPropertyValueJson rpvj = (RenderPropertyValueJson)woko.getFacet("renderPropertyValueJson_" + propertyName, request, owner);
    if (rpvj==null) {
      // type-specific
      rpvj = (RenderPropertyValueJson)woko.getFacet("renderPropertyValueJson", request, value);
    }
    if (rpvj==null) {
      // default to toString()
      return value.toString();
    }
    return rpvj.propertyToJson(request, value);
  }

}
