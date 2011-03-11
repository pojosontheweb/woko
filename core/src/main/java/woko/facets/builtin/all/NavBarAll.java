package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.NavBar;

@FacetKey(name="navBar",profileId="all")
public class NavBarAll extends BaseFragmentFacet implements NavBar {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/navBar.jsp";
  }


}
