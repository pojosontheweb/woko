package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderLinks;
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

  public Object getLinks() {
    List<Map<String,String>> links = new ArrayList<Map<String, String>>();
    WokoFacetContext facetContext = getFacetContext();
    Woko woko = getFacetContext().getWoko();
    Object o = facetContext.getTargetObject();
    Class<?> oc = o.getClass();
    HttpServletRequest request = getRequest();
    ObjectStore store = woko.getObjectStore();

    // display edit link if object can be edited
    Object editFacet = woko.getFacet("edit", request, o, oc);
    if (editFacet!=null) {
      String className = store.getClassMapping(oc);
      String key = store.getKey(o);
      Map<String,String> m = new HashMap<String,String>();
      m.put("edit/" + className + "/" + key, "Edit");
      links.add(m);
    }

    Object deleteFacet = woko.getFacet("delete", request, o, oc);
    if (deleteFacet!=null) {
      String className = store.getClassMapping(oc);
      String key = store.getKey(o);
      Map<String,String> m = new HashMap<String,String>();
      m.put("delete/" + className + "/" + key, "Delete");
      links.add(m);
    }
    return links;
  }

}
