package woko.facets.builtin.auth;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseResolutionFacet;

import javax.servlet.http.HttpServletRequest;

@FacetKeyList(keys ={
    @FacetKey(name = "view", profileId = "guest"),
    @FacetKey(name = "edit", profileId = "guest"),
    @FacetKey(name = "create", profileId = "guest"),
    @FacetKey(name = "delete", profileId = "guest"),
    @FacetKey(name = "save", profileId = "guest"),
    @FacetKey(name = "json", profileId = "guest"),
    @FacetKey(name = "find", profileId = "guest"),
    @FacetKey(name = "list", profileId = "guest"),
    @FacetKey(name = "search", profileId = "guest"),
    @FacetKey(name = "studio", profileId = "guest")
})
public class LoginRequiredFacet extends BaseResolutionFacet {

  public Resolution getResolution(ActionBeanContext abc) {
    HttpServletRequest request = abc.getRequest();
    String requestedPage = request.getRequestURI();
    // strip context path if needed
    String contextPath = request.getContextPath();
    if (requestedPage.startsWith(contextPath)) {
      requestedPage = requestedPage.substring(contextPath.length());
    }
    String queryString = request.getQueryString();
    if (queryString != null) {
      requestedPage += "?" + queryString;
    }
    return new ForwardResolution("/login").addParameter("targetUrl", requestedPage);
  }


}
