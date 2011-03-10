package woko.facets;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseFragmentFacet extends BaseFacet implements FragmentFacet {

  private HttpServletRequest request;

  public String getFragmentPath(HttpServletRequest request) {
    this.request = request;
    return this.getPath();
  }

  public abstract String getPath();

  HttpServletRequest getRequest() {
    return request;
  }

}
