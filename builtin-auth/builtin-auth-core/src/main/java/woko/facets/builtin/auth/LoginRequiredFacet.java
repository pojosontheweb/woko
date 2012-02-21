package woko.facets.builtin.auth;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.WokoFacets;

import javax.servlet.http.HttpServletRequest;

@FacetKeyList(keys ={
    @FacetKey(name = WokoFacets.view, profileId = "guest"),
    @FacetKey(name = WokoFacets.edit, profileId = "guest"),
    @FacetKey(name = WokoFacets.create, profileId = "guest"),
    @FacetKey(name = WokoFacets.delete, profileId = "guest"),
    @FacetKey(name = WokoFacets.save, profileId = "guest"),
    @FacetKey(name = WokoFacets.json, profileId = "guest"),
    @FacetKey(name = WokoFacets.find, profileId = "guest"),
    @FacetKey(name = WokoFacets.list, profileId = "guest"),
    @FacetKey(name = WokoFacets.search, profileId = "all"),
    @FacetKey(name = WokoFacets.studio, profileId = "guest")
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
