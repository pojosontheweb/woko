package woko.facets;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IInstanceFacet;
import woko.facets.WokoFacetContext;

public abstract class BaseFacet implements IFacet, IInstanceFacet {

  private WokoFacetContext facetContext;

  boolean acceptNullTargetObject = true;

  public boolean isAcceptNullTargetObject() {
    return acceptNullTargetObject;
  }

  public void setAcceptNullTargetObject(boolean acceptNullTargetObject) {
    this.acceptNullTargetObject = acceptNullTargetObject;
  }

  public WokoFacetContext getFacetContext() {
    return facetContext;
  }

  public void setFacetContext(IFacetContext iFacetContext) {
    this.facetContext = (WokoFacetContext)iFacetContext;
  }

  public boolean matchesTargetObject(Object targetObject) {
    if (!acceptNullTargetObject && targetObject==null) {
      return false;
    }
    return true;
  }

}
