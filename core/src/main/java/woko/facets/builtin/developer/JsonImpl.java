package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import org.json.JSONObject;
import woko.facets.BaseFacet;
import woko.facets.BaseResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Json;
import woko.facets.builtin.RenderObjectJson;

@FacetKey(name="json", profileId="developer")
public class JsonImpl extends BaseResolutionFacet implements Json {

  public Resolution getResolution() {
    ActionBeanContext abc = getContext();
    WokoFacetContext facetContext = getFacetContext();
    RenderObjectJson roj =
        (RenderObjectJson)facetContext.getWoko().getFacet("renderObjectJson", abc.getRequest(), facetContext.getTargetObject());
    JSONObject json = roj.objectToJson(abc.getRequest());
    String jsonStr = json.toString();
    return new StreamingResolution("text/json", jsonStr);
  }


}
