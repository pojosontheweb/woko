package woko.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseFragmentFacet
import woko.facets.builtin.RenderPropertyName

@FacetKey(name='renderPropertyName', profileId='all')
class RenderPropertyNameImpl extends BaseFragmentFacet implements RenderPropertyName {

  String propertyName

}
