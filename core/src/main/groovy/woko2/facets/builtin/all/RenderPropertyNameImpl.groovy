package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseFragmentFacet

@FacetKey(name='renderPropertyName', profileId='all')
class RenderPropertyNameImpl extends BaseFragmentFacet implements RenderPropertyName {

  String propertyName

}
