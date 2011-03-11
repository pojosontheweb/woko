package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.builtin.Home;

@FacetKey(name="home", profileId="all")
public class HomeImpl extends BaseForwardResolutionFacet implements Home {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/home.jsp";
  }

}