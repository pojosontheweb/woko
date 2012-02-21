package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.*;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FacetKey(name="renderLinks", profileId="all")
public class RenderLinksImpl extends BaseFragmentFacet implements RenderLinks {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderLinks.jsp";
  }

  public List<Link> getLinks() {
    List<Link> links = new ArrayList<Link>();
    WokoFacetContext facetContext = getFacetContext();
    Woko woko = getFacetContext().getWoko();
    Object o = facetContext.getTargetObject();
    Class<?> oc = o.getClass();
    HttpServletRequest request = getRequest();
    ObjectStore store = woko.getObjectStore();

    // display edit link if object can be edited (use instanceof because could be a login required facet)
    Object editFacet = woko.getFacet(WokoFacets.edit, request, o, oc);
    if (editFacet instanceof Edit) {
      String className = store.getClassMapping(oc);
      String key = store.getKey(o);
      if (key!=null) {
        links.add(new Link(WokoFacets.edit + "/" + className + "/" + key, "Edit").setCssClass("edit"));
      }
    }

    Object deleteFacet = woko.getFacet(WokoFacets.delete, request, o, oc);
    if (deleteFacet instanceof Delete) {
      String className = store.getClassMapping(oc);
      String key = store.getKey(o);
      if (key!=null) {
        links.add(new Link(WokoFacets.delete + "/" + className + "/" + key, "Delete").setCssClass("delete"));
      }
    }

    Object jsonFacet = woko.getFacet(WokoFacets.json, request, o, oc);
    if (jsonFacet instanceof Json) {
      String className = store.getClassMapping(oc);
      String key = store.getKey(o);
      if (key!=null) {
        links.add(new Link(WokoFacets.json + "/" + className + "/" + key, "Json").setCssClass("json"));
      }
    }

    return links;
  }

}
