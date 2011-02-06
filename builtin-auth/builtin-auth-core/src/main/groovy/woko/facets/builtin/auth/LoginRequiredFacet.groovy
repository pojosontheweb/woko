package woko.facets.builtin.auth

import woko.facets.BaseResolutionFacet
import net.sourceforge.stripes.action.Resolution
import net.sourceforge.stripes.action.ActionBeanContext

import javax.servlet.http.HttpServletRequest
import net.sourceforge.stripes.action.ForwardResolution
import net.sourceforge.jfacets.annotations.FacetKey
import net.sourceforge.jfacets.annotations.FacetKeyList

@FacetKeyList(keys = [
  @FacetKey(name = 'view', profileId='all'),
  @FacetKey(name = 'edit', profileId='all'),
  @FacetKey(name = 'delete', profileId='all'),
  @FacetKey(name = 'save', profileId='all'),
  @FacetKey(name = 'json', profileId='all'),
  @FacetKey(name = 'find', profileId='all'),
  @FacetKey(name = 'list', profileId='all'),
  @FacetKey(name = 'search', profileId='all'),
  @FacetKey(name = 'studio', profileId='all')
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
        requestedPage += queryString;
    }
    return new ForwardResolution("/login").addParameter("targetUrl", requestedPage);
  }


}
