package woko2.facets.builtin.developer

import woko2.facets.BaseFacet
import woko2.facets.builtin.Json
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext
import woko2.facets.builtin.RenderObjectJson
import org.json.JSONObject
import net.sourceforge.stripes.action.StreamingResolution
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name='json', profileId='developer')
class JsonImpl extends BaseFacet implements Json {

  def Resolution getResolution(ActionBeanContext abc) {
    RenderObjectJson roj = (RenderObjectJson)context.woko.getFacet(RenderObjectJson.name, abc.request, context.targetObject)
    JSONObject json = roj.objectToJson(abc.request)
    String jsonStr = json.toString()
    return new StreamingResolution('text/json', jsonStr)
  }


}
