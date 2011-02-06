package woko.facets.builtin.developer

import woko.facets.BaseFacet
import woko.facets.builtin.Json
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import woko.facets.builtin.RenderObjectJson
import org.json.JSONObject
import net.sourceforge.stripes.action.StreamingResolution
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name='json', profileId='developer')
class JsonImpl extends BaseFacet implements Json {

  def Resolution getResolution(ActionBeanContext abc) {
    RenderObjectJson roj = (RenderObjectJson)facetContext.woko.getFacet(RenderObjectJson.name, abc.request, facetContext.targetObject)
    JSONObject json = roj.objectToJson(abc.request)
    String jsonStr = json.toString()
    return new StreamingResolution('text/json', jsonStr)
  }


}
