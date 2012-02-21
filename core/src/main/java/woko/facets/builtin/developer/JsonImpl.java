package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONObject;
import woko.facets.BaseFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Json;
import woko.facets.builtin.RenderObjectJson;
import woko.facets.builtin.WokoFacets;

@FacetKey(name= WokoFacets.json, profileId="developer")
public class JsonImpl extends BaseFacet implements Json {

  public Resolution getResolution(ActionBeanContext abc) {
    WokoFacetContext facetContext = getFacetContext();
    RenderObjectJson roj =
        (RenderObjectJson)facetContext.getWoko().getFacet("renderObjectJson", abc.getRequest(), facetContext.getTargetObject());
    JSONObject json = roj.objectToJson(abc.getRequest());
    String jsonStr = json.toString();
    return new StreamingResolution("text/json", jsonStr);
  }


}
