package woko.facets.builtin.all

import woko.util.Util
import net.sourceforge.jfacets.annotations.FacetKey
import woko.facets.BaseFragmentFacet
import net.sourceforge.jfacets.IFacetContext
import woko.facets.builtin.RenderProperties

@FacetKey(name='renderProperties', profileId='all')
class RenderPropertiesImpl extends BaseFragmentFacet implements RenderProperties {

  List<String> propertyNames
  Map<String,Object> propertyValues

  String getPath() {
    return '/WEB-INF/woko/jsp/all/renderProperties.jsp'
  }

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
