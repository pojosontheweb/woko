package woko2.facets

import javax.servlet.http.HttpServletRequest

public interface FragmentFacet {

  String getFragmentPath(HttpServletRequest request)

}