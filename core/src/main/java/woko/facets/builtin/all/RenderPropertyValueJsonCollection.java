package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import org.json.JSONArray;
import org.json.JSONObject;
import woko.facets.BaseFacet;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.WokoFacets;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@FacetKey(name= WokoFacets.renderPropertyValueJson, profileId="all", targetObjectType=Collection.class)
public class RenderPropertyValueJsonCollection extends BaseFacet implements RenderPropertyValueJson {

  private static final WLogger logger = WLogger.getLogger(RenderPropertyValueJsonCollection.class);

  public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    JSONArray arr = new JSONArray();
    Collection<?> c = (Collection<?>)propertyValue;
    for (Object item : c) {
      logger.debug("converting collection item $item to json...");
      if (item==null) {
        arr.put(new JSONObject());
      }
      RenderPropertyValueJson rpvj =
          (RenderPropertyValueJson)getFacetContext().getWoko().getFacet(WokoFacets.renderPropertyValueJson, request, item);
      if (rpvj==null) {
        logger.debug("... no renderPropertyValueJson facet found for collection item, adding empty JSON Object");
        arr.put(new JSONObject());
      } else {
        Object json = rpvj.propertyToJson(request, item);
        logger.debug("... converted item $item to $json");
        arr.put(json);
      }
    }
    return arr;
  }

}
