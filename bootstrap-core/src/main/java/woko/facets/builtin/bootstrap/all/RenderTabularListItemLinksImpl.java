package woko.facets.builtin.bootstrap.all;


import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.facets.BaseFragmentFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.RenderLinks;
import woko.facets.builtin.RenderTabularListItemLinks;
import woko.facets.builtin.View;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.all.Link;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;
import woko.util.LinkUtil;
import woko.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Generic <code>renderTabularListItemLinks</code> facet for unauthenticated users. Tries to retrieve
 * <code>view</code> facet and all links contained by the <code>renderLinks</code> facet in order to create the links
 * to be displayed for the target object.
 */
@FacetKey(name = WokoFacets.renderTabularListItemLinks, profileId = "all")
public class RenderTabularListItemLinksImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> implements RenderTabularListItemLinks{

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/all/renderTabularListItemLinks.jsp";

    @Override
    public String getPath() {
        return FRAGMENT_PATH;
    }

    @Override
    public List<Link> getLinks() {
        List<Link> links = new ArrayList<Link>();
        WokoFacetContext<OsType,UmType,UnsType,FdmType> facetContext = getFacetContext();
        Woko<OsType,UmType,UnsType,FdmType> woko = getFacetContext().getWoko();
        Object o = facetContext.getTargetObject();
        if (o!=null) {
            OsType store = woko.getObjectStore();
            Class<?> oc = store.getObjectClass(o);
            HttpServletRequest request = getRequest();
            Locale locale = request.getLocale();

            // display view link if object can be displayed
            Object viewFacet = woko.getFacet(WokoFacets.view, request, o, oc);
            if (viewFacet instanceof View) {
                String key = store.getKey(o);
                if (key != null) {
                    String url = LinkUtil.getUrl(woko, o, View.FACET_NAME);
                    links.add(new Link(url, Util.getMessage(locale, "woko.links.view")).setCssClass("link-view"));
                }
            }
            // grab available links on the target object
            Object renderLinksFacet = woko.getFacet(WokoFacets.renderLinks, request, o, oc);
            if (renderLinksFacet instanceof RenderLinks){
                links.addAll(((RenderLinks) renderLinksFacet).getLinks());
            }
        }

        return links;
    }
}
