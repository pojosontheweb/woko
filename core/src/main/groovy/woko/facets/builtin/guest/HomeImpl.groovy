package woko.facets.builtin.guest

import woko.facets.BaseForwardResolutionFacet
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.builtin.Home

@FacetKey(name='home', profileId='guest')
class HomeImpl extends BaseForwardResolutionFacet implements Home {

}
