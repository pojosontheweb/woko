package woko2.facets

import net.sourceforge.jfacets.IFacet
import net.sourceforge.jfacets.IFacetContext
import net.sourceforge.jfacets.IInstanceFacet

abstract class BaseFacet implements IFacet, IInstanceFacet {

  private WokoFacetContext context

  boolean acceptNullTargetObject = true

  WokoFacetContext getContext() {
    return context
  }

  void setContext(IFacetContext iFacetContext) {
    this.context = (WokoFacetContext)iFacetContext
  }

  boolean matchesTargetObject(Object targetObject) {
    if (!acceptNullTargetObject && targetObject==null) {
      return false
    }
    return true
  }

  protected String computeJspPathFromFacetDescriptor() {
    return new StringBuilder('/WEB-INF/woko/jsp/').
      append(context.facetDescriptor.profileId).
      append('/').
      append(context.facetDescriptor.name).
      append(".jsp")
  }

}
