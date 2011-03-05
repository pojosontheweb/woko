package woko.facets.builtin.auth

import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext

import javax.servlet.http.HttpServletRequest
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.jfacets.annotations.FacetKeyList

@FacetKeyList(keys = [
  @FacetKey(name = 'view', profileId='guest'),
  @FacetKey(name = 'edit', profileId='guest'),
  @FacetKey(name = 'create', profileId='guest'),
  @FacetKey(name = 'delete', profileId='guest'),
  @FacetKey(name = 'save', profileId='guest'),
  @FacetKey(name = 'json', profileId='guest'),
  @FacetKey(name = 'find', profileId='guest'),
  @FacetKey(name = 'list', profileId='guest'),
  @FacetKey(name = 'search', profileId='guest'),
  @FacetKey(name = 'studio', profileId='guest')
])
class LoginRequiredFacet extends BaseResolutionFacet {

  Resolution getResolution(ActionBeanContext abc) {
    HttpServletRequest request = abc.request
    String requestedPage = request.requestURI
    // strip context path if needed
    String contextPath = request.contextPath
    if (requestedPage.startsWith(contextPath)) {
        requestedPage = requestedPage.substring(contextPath.length());
    }
    String queryString = request.queryString
    if (queryString!=null) {
        requestedPage += "?" + queryString;
    }
    return new ForwardResolution("/login").addParameter("targetUrl", requestedPage);
  }


}
