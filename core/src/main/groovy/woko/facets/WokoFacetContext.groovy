package woko.facets

import net.sourceforge.jfacets.impl.DefaultFacetContext
import woko.Woko
import net.sourceforge.jfacets.IProfile
import net.sourceforge.jfacets.FacetDescriptor
import javax.servlet.http.HttpServletRequest

class WokoFacetContext extends DefaultFacetContext {

  private Woko woko
  private HttpServletRequest request

  def WokoFacetContext(
      String facetName,
      IProfile profile,
      Object targetObject,
      FacetDescriptor facetDescriptor,
      Woko woko,
      HttpServletRequest request) {
    super(facetName, profile, targetObject, facetDescriptor)
    this.woko = woko
    this.request = request
  }

  Woko getWoko() {
    return woko
  }

  HttpServletRequest getRequest() {
    return request
  }

}
