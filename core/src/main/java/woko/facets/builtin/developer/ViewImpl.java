package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.BaseForwardRpcResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Json;
import woko.facets.builtin.View;

@FacetKey(name="view", profileId="developer")
public class ViewImpl extends BaseForwardRpcResolutionFacet implements View {

  public String getPath() {
    return "/WEB-INF/woko/jsp/developer/view.jsp";
  }

    @Override
    protected Resolution getRpcResolution(ActionBeanContext abc) {
        WokoFacetContext wokoFacetContext = getFacetContext();
        Json json = (Json)wokoFacetContext.getWoko().getFacet("json", wokoFacetContext.getRequest(), wokoFacetContext.getTargetObject());
        return json==null ? null : json.getResolution(abc);
    }
}
