package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.NavBar;

@FacetKey(name="navBar",profileId="developer")
public class NavBarDev extends BaseFragmentFacet implements NavBar {

  public String getPath() {
    return "/WEB-INF/woko/jsp/developer/navBar.jsp";
  }


}