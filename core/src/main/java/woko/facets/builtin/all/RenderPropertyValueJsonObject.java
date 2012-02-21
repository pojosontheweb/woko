package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import org.json.JSONException;
import org.json.JSONObject;
import woko.Woko;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderPropertyValueJson;
import woko.facets.builtin.RenderTitle;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;

@FacetKey(name= WokoFacets.renderPropertyValueJson, profileId="all")
public class RenderPropertyValueJsonObject extends BaseFacet implements RenderPropertyValueJson {

  public Object propertyToJson(HttpServletRequest request, Object propertyValue) {
    try {
      if (propertyValue==null) {
        return null;
      }
      JSONObject res = new JSONObject();
      res.put("_object", true);
      Class c = propertyValue.getClass();
      WokoFacetContext facetContext = getFacetContext();
      Woko woko = facetContext.getWoko();
      ObjectStore os = woko.getObjectStore();
      String className = os.getClassMapping(c);
      res.put("_className", className);
      String key = os.getKey(propertyValue);
      if (key!=null) {
        res.put("_persistent", true);
        res.put("_key", key);
      }

      RenderTitle renderTitle = (RenderTitle)woko.getFacet("renderTitle", request, propertyValue);
      if (renderTitle!=null) {
        res.put("_title", renderTitle.getTitle());
      }

      return res;
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

}
