package woko.facets;


import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.impl.DefaultFacetContext;
import woko.Woko;

import javax.servlet.http.HttpServletRequest;

public class WokoFacetContext extends DefaultFacetContext {

  private final Woko woko;
  private final HttpServletRequest request;

  public WokoFacetContext(
      String facetName,
      IProfile profile,
      Object targetObject,
      FacetDescriptor facetDescriptor,
      Woko woko,
      HttpServletRequest request) {
    super(facetName, profile, targetObject, facetDescriptor);
    this.woko = woko;
    this.request = request;
  }

  public Woko getWoko() {
    return woko;
  }

  public HttpServletRequest getRequest() {
    return request;
  }

}
