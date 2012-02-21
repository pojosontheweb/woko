package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderLinks;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FacetKey(name="renderLinksEdit", profileId="all")
public class RenderLinksEditImpl extends BaseFragmentFacet implements RenderLinks {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderLinksEdit.jsp";
  }

  public List<Link> getLinks() {
    List<Link> links = new ArrayList<Link>();
    WokoFacetContext  facetContext = getFacetContext();
    Woko woko = getFacetContext().getWoko();
    Object o = facetContext.getTargetObject();
    Class<?> oc = o.getClass();
    HttpServletRequest request = getRequest();
    ObjectStore store = woko.getObjectStore();

    // display view link if object can be displayed
    Object viewFacet = woko.getFacet(WokoFacets.view, request, o, oc);
    if (viewFacet!=null) {
      String className = store.getClassMapping(oc);
      String key = store.getKey(o);
      if (key!=null) {
        links.add(new Link(WokoFacets.view + "/" + className + "/" + key, "Close editing").setCssClass("close"));
      }
    }

    Object deleteFacet = woko.getFacet(WokoFacets.delete, request, o, oc);
    if (deleteFacet!=null) {
      String className = store.getClassMapping(oc);
      String key = store.getKey(o);
      if (key!=null) {
        links.add(new Link(WokoFacets.delete + "/" + className + "/" + key, "Delete").setCssClass("delete"));
      }
    }
    return links;
  }

}
