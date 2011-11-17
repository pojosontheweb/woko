package woko.facets;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IInstanceFacet;
import woko.Woko;
import woko.facets.WokoFacetContext;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseFacet implements IFacet {

  private WokoFacetContext facetContext;

  public WokoFacetContext getFacetContext() {
    return facetContext;
  }

  public void setFacetContext(IFacetContext iFacetContext) {
    this.facetContext = (WokoFacetContext)iFacetContext;
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
