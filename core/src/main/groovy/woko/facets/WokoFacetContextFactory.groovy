package woko.facets

import net.sourceforge.jfacets.IFacetContextFactory
import net.sourceforge.jfacets.IFacetContext
import net.sourceforge.jfacets.IProfile
import net.sourceforge.jfacets.FacetDescriptor
import woko.Woko
import javax.servlet.http.HttpServletRequest
import woko.actions.WokoRequestInterceptor

class WokoFacetContextFactory implements IFacetContextFactory {

  private final Woko woko

  private ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>()

  def WokoFacetContextFactory(Woko woko) {
    this.woko = woko;
  }

  IFacetContext create(String name, IProfile profile, Object targetObject, FacetDescriptor facetDescriptor) {
    return new WokoFacetContext(name, profile, targetObject, facetDescriptor, woko, WokoRequestInterceptor.getRequest())
  }

  
}
