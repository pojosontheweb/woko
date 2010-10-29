package woko2.facets.builtin.all

import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseFragmentFacet

@FacetKey(name='renderPropertyValue', profileId='all')
class RenderPropertyValueImpl extends BaseFragmentFacet implements RenderPropertyValue {

  Object owningObject
  String propertyName

}
