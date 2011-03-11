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

@FacetKey(name="renderLinksEdit", profileId="all")
public class RenderLinksEditImpl extends BaseFragmentFacet implements RenderLinks {

  public String getPath() {
    return "/WEB-INF/woko/jsp/all/renderLinksEdit.jsp";
  }

  public Object getLinks() {
    List<Map<String,String>> links = new ArrayList<Map<String, String>>();
    WokoFacetContext  facetContext = getFacetContext();
    Woko woko = getFacetContext().getWoko();
    Object o = facetContext.getTargetObject();
    Class<?> oc = o.getClass();
    HttpServletRequest request = getRequest();
    ObjectStore store = woko.getObjectStore();

    // display view link if object can be displayed
    Object viewFacet = woko.getFacet("view", request, o, oc);
    if (viewFacet!=null) {
      String className = store.getClassMapping(oc);
      String key = store.getKey(o);
      Map<String,String> m = new HashMap<String,String>();
      m.put("view/" + className + "/" + key, "Close editing");
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
