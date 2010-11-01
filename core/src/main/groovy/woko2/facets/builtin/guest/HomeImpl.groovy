package woko2.facets.builtin.guest

import woko2.facets.BaseForwardResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.builtin.Home

@FacetKey(name='home', profileId='guest')
class HomeImpl extends BaseForwardResolutionFacet implements Home {

}
