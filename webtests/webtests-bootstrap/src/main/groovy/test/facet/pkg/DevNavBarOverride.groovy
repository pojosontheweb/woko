package test.facet.pkg

import woko.facets.builtin.developer.NavBarDev
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.WokoFacets

@FacetKey(name=WokoFacets.navBar,profileId="developer")
class DevNavBarOverride extends NavBarDev {

    @Override
    String getPath() {
        return '/WEB-INF/jsp/myNavBarDev.jsp'
    }


}
