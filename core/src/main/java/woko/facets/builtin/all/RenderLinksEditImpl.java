package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Delete;
import woko.facets.builtin.RenderLinks;
import woko.facets.builtin.View;
import woko.facets.builtin.WokoFacets;
import woko.persistence.ObjectStore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@FacetKey(name = WokoFacets.renderLinksEdit, profileId = "all")
public class RenderLinksEditImpl extends BaseFragmentFacet implements RenderLinks {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderLinksEdit.jsp";

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public List<Link> getLinks() {
        List<Link> links = new ArrayList<Link>();
        WokoFacetContext facetContext = getFacetContext();
        Woko woko = getFacetContext().getWoko();
        Object o = facetContext.getTargetObject();
        Class<?> oc = o.getClass();
        HttpServletRequest request = getRequest();
        ObjectStore store = woko.getObjectStore();

        // display view link if object can be displayed
        Object viewFacet = woko.getFacet(WokoFacets.view, request, o, oc);
        if (viewFacet instanceof View) {
            String className = store.getClassMapping(oc);
            String key = store.getKey(o);
            if (key != null) {
                links.add(new Link(WokoFacets.view + "/" + className + "/" + key, "Close editing").setCssClass("close"));
            }
        }

        Object deleteFacet = woko.getFacet(WokoFacets.delete, request, o, oc);
        if (deleteFacet instanceof Delete) {
            String className = store.getClassMapping(oc);
            String key = store.getKey(o);
            if (key != null) {
                links.add(new Link(WokoFacets.delete + "/" + className + "/" + key, "Delete").setCssClass("delete"));
            }
        }
        return links;
    }

}
