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

  void setContext(IFacetContext iFacetContext) {
    super.setContext(iFacetContext)
    def obj = context.targetObject
    propertyNames = Util.getPropertyNames(obj, ["metaClass"])
    propertyValues = new HashMap<String,Object>()
    for (String pName : propertyNames) {
      propertyValues.put(pName, Util.getPropertyValue(obj, pName))
    }
  }

}
