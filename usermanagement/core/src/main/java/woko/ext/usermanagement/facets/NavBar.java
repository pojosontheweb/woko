package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.developer.NavBarDev;

@FacetKey(name = WokoFacets.navBar, profileId = "developer")
public class NavBar extends NavBarDev {

    @Override
    public String getPath() {
        return "/WEB-INF/jsp/woko/usermanagement/navBarDev.jsp";
    }
}
