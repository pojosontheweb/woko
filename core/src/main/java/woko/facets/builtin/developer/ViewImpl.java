package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.builtin.View;

@FacetKey(name="view", profileId="developer")
public class ViewImpl extends BaseForwardResolutionFacet implements View {

  public ViewImpl() {
    setAcceptNullTargetObject(false);
  }

  public String getPath() {
    return "/WEB-INF/woko/jsp/developer/view.jsp";
  }
}
