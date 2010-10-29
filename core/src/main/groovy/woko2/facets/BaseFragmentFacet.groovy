package woko2.facets

import javax.servlet.http.HttpServletRequest

abstract class BaseFragmentFacet extends BaseFacet implements FragmentFacet {

  private HttpServletRequest request

  def String getFragmentPath(HttpServletRequest request) {
    this.request = request
    return computeJspPathFromFacetDescriptor()
  }

  HttpServletRequest getRequest() {
    return request
  }

}
