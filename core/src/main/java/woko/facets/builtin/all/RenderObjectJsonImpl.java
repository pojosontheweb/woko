package woko.facets.builtin.all;


import net.sourceforge.jfacets.annotations.FacetKey;
import org.json.JSONException;
import org.json.JSONObject;
import woko.Woko;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.*;
import woko.persistence.ObjectStore;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@FacetKey(name = WokoFacets.renderObjectJson, profileId = "all")
public class RenderObjectJsonImpl extends BaseFacet implements RenderObjectJson {

    private static final WLogger logger = WLogger.getLogger(RenderObjectJsonImpl.class);

    public JSONObject objectToJson(HttpServletRequest request) {
        WokoFacetContext facetContext = getFacetContext();
        Object o = facetContext.getTargetObject();
        try {
            if (o == null) {
                logger.debug("Object is null, returning null");
                return null;
            }
            JSONObject result = new JSONObject();
            Woko woko = facetContext.getWoko();
            // find props to be rendered using renderProperties facet
            RenderProperties renderProperties = (RenderProperties) woko.getFacet(WokoFacets.renderProperties, request, o);
            if (renderProperties == null) {
                logger.warn("No renderProperties facet found for targetObject $o, as a result no props will be serialized.");
                return result;
            }

            // convert the properties to JSON
            Map<String, Object> propNamesAndValues = renderProperties.getPropertyValues();
            for (String k : propNamesAndValues.keySet()) {
                Object v = propNamesAndValues.get(k);
                Object jsonValue = propertyToJson(request, o, k, v);
                logger.debug("Converted prop $k to json $jsonValue");
                result.put(k, jsonValue);
            }

            // add object metadata
            addWokoMetadata(woko, result, o, request);
            return result;
        } catch (JSONException e) {
            logger.error("Unable to convert object to JSON : " + o);
            throw new RuntimeException(e);
        }
    }

    public static void addWokoMetadata(Woko woko, JSONObject json, Object obj, HttpServletRequest request) {
        if (obj!=null) {
            try {
                JSONObject metadata = new JSONObject();
                ObjectStore os = woko.getObjectStore();
                String className = os.getClassMapping(obj.getClass());
                metadata.put("className", className);
                String key = os.getKey(obj);
                if (key != null) {
                    metadata.put("key", key);
                }
                RenderTitle renderTitle = (RenderTitle) woko.getFacet(WokoFacets.renderTitle, request, obj);
                if (renderTitle != null) {
                    metadata.put("title", renderTitle.getTitle());
                }
                json.put("_wokoInfo", metadata);
            } catch(JSONException e) {
                logger.error("Unable to add metadata to JSON : " + json + " for object " + obj);
                throw new RuntimeException(e);
            }
        }
    }

    Object propertyToJson(HttpServletRequest request, Object owner, String propertyName, Object value) {
        if (value == null) {
            return null;
        }
        WokoFacetContext facetContext = getFacetContext();
        Woko woko = facetContext.getWoko();
        // try name-specific first
        RenderPropertyValueJson rpvj = (RenderPropertyValueJson) woko.getFacet("renderPropertyValueJson_" + propertyName, request, owner);
        if (rpvj == null) {
            // type-specific
            rpvj = (RenderPropertyValueJson) woko.getFacet(WokoFacets.renderPropertyValueJson, request, value);
        }
        if (rpvj == null) {
            // default to toString()
            return value.toString();
        }
        return rpvj.propertyToJson(request, value);
    }

}
