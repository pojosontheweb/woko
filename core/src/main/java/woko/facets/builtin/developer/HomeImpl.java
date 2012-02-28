package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.builtin.Home;
import woko.facets.builtin.WokoFacets;

@FacetKey(name = WokoFacets.home, profileId = "developer")
public class HomeImpl extends BaseForwardResolutionFacet implements Home {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/home.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

}
