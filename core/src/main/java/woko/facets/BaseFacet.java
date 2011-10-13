package woko.facets;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IInstanceFacet;
import woko.Woko;
import woko.facets.WokoFacetContext;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;

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

    public Woko getWoko() {
        return getFacetContext().getWoko();
    }

    public ObjectStore getObjectStore() {
        return getWoko().getObjectStore();
    }

    public HttpServletRequest getRequest() {
        return facetContext.getRequest();
    }

}
