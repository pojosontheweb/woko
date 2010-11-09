package woko2.facets.builtin.all

import woko2.util.Util
import net.sourceforge.jfacets.annotations.FacetKey
import woko2.facets.BaseFragmentFacet
import net.sourceforge.jfacets.IFacetContext
import woko2.facets.builtin.RenderProperties

@FacetKey(name='renderProperties', profileId='all')
class RenderPropertiesImpl extends BaseFragmentFacet implements RenderProperties {

  List<String> propertyNames
  Map<String,Object> propertyValues

  void setFacetContext(IFacetContext iFacetContext) {
    super.setFacetContext(iFacetContext)
    def obj = facetContext.targetObject
    propertyNames = Util.getPropertyNames(obj, ["metaClass"])
    propertyValues = new HashMap<String,Object>()
    for (String pName : propertyNames) {
      propertyValues.put(pName, Util.getPropertyValue(obj, pName))
    }
  }

}
