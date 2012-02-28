package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.NavBar;
import woko.facets.builtin.WokoFacets;

@FacetKey(name = WokoFacets.navBar, profileId = "developer")
public class NavBarDev extends BaseFragmentFacet implements NavBar {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/navBar.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }


}
