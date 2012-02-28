package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;
import woko.facets.builtin.Home;
import woko.facets.builtin.WokoFacets;

@FacetKey(name= WokoFacets.home, profileId="all")
public class HomeImpl extends BaseForwardResolutionFacet implements Home {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/home.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

}