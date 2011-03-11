package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.builtin.Home;

@FacetKey(name="home", profileId="developer")
public class HomeImpl extends BaseForwardResolutionFacet implements Home {

  public String getPath() {
    return "/WEB-INF/woko/jsp/developer/home.jsp";
  }

}