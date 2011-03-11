package woko.facets;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IFacetContextFactory;
import net.sourceforge.jfacets.IProfile;
import woko.Woko;
import woko.actions.WokoRequestInterceptor;

import javax.servlet.http.HttpServletRequest;

public class WokoFacetContextFactory implements IFacetContextFactory {

  private final Woko woko;

  private ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();

  public WokoFacetContextFactory(Woko woko) {
    this.woko = woko;
  }

  public IFacetContext create(String name, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor) {
    return new WokoFacetContext(
        name,
        profile,
        targetObject,
        facetDescriptor,
        woko,
        WokoRequestInterceptor.getRequest());
  }


}
