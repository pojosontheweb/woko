package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseFragmentFacet
import woko2.facets.builtin.NavBar

@FacetKey(name='navBar',profileId='developer')
class NavBarDev extends BaseFragmentFacet implements NavBar {
}
