package woko2.facets.builtin.developer

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseForwardResolutionFacet
import woko2.facets.builtin.Home

@FacetKey(name='home', profileId='developer')
class HomeImpl extends BaseForwardResolutionFacet implements Home {

}
