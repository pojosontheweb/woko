package test.facet.pkg

import woko.facets.builtin.developer.NavBarDev
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name="navBar",profileId="developer")
class DevNavBarOverride extends NavBarDev {

    @Override
    String getPath() {
        return '/WEB-INF/jsp/myNavBarDev.jsp'
    }


}
