package woko2.facets

import net.sourceforge.jfacets.IFacet
import net.sourceforge.jfacets.IFacetContext
import net.sourceforge.jfacets.IInstanceFacet

abstract class BaseFacet implements IFacet, IInstanceFacet {

  private WokoFacetContext facetContext

  boolean acceptNullTargetObject = true

  WokoFacetContext getFacetContext() {
    return facetContext
  }

  void setFacetContext(IFacetContext iFacetContext) {
    this.facetContext = (WokoFacetContext)iFacetContext
  }

  boolean matchesTargetObject(Object targetObject) {
    if (!acceptNullTargetObject && targetObject==null) {
      return false
    }
    return true
  }

  protected String computeJspPathFromFacetDescriptor() {
    return new StringBuilder('/WEB-INF/woko/jsp/').
      append(facetContext.facetDescriptor.profileId).
      append('/').
      append(facetContext.facetDescriptor.name).
      append(".jsp")
  }

}
