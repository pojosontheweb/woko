package woko.facets.builtin.all

import woko.facets.BaseFragmentFacet
import woko.facets.builtin.NavBar
import net.sourceforge.jfacets.annotations.FacetKey

@FacetKey(name='navBar',profileId='all')
class NavBarAll extends BaseFragmentFacet implements NavBar {

  String getPath() {
    return '/WEB-INF/woko/jsp/all/navBar.jsp'
  }


}
