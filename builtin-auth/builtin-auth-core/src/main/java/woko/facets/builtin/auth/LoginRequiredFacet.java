package woko.facets.builtin.auth;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.WokoFacets;

import javax.servlet.http.HttpServletRequest;

/**
 * Facet that forces the user to log-in. This facet is assigned by default
 * to all CRUD facets for <code>guest</code> role, to that when a guest user tries to
 * reach a "login required" facet, (s)he is prompted to authenticate first.
 *
 * You can override your own facets for your own role so that guest users are
 * forced to login when they try to reach some facets, instead of getting a 404 (FacetNotFound).
 */
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
