package woko.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseForwardResolutionFacet
import woko.facets.builtin.Home

@FacetKey(name='home', profileId='developer')
class HomeImpl extends BaseForwardResolutionFacet implements Home {

}
