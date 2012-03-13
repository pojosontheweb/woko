package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.rpc.RpcResolutionWrapper;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Delete;
import woko.facets.builtin.WokoFacets;

@FacetKey(name = WokoFacets.delete, profileId = "developer")
public class DeleteImpl extends BaseResolutionFacet implements Delete {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/confirmDelete.jsp";
    private String confirm;
    private String cancel;

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    private static StreamingResolution createResolution(boolean success) {
        return new StreamingResolution("text/json", "{ \"success\": " + success + " }");
    }

    public Resolution getResolution(final ActionBeanContext abc) {
        if (cancel != null) {
            WokoFacetContext facetContext = getFacetContext();
            abc.getMessages().add(new LocalizableMessage("woko.devel.delete.cancel"));
            return new RpcResolutionWrapper(new RedirectResolution(
                    facetContext.getWoko().facetUrl(
                            WokoFacets.view,
                            facetContext.getTargetObject()))) {
                @Override
                public Resolution getRpcResolution() {
                    return createResolution(false);
                }
            };
        }
        if (confirm != null) {
            final WokoFacetContext facetContext = getFacetContext();
            final Woko woko = facetContext.getWoko();
            final Object targetObject = facetContext.getTargetObject();
            woko.getObjectStore().delete(targetObject);
            abc.getMessages().add(new LocalizableMessage("woko.devel.delete.confirm"));
            return new RpcResolutionWrapper(new RedirectResolution("/home")) {
                @Override
                public Resolution getRpcResolution() {
                    return createResolution(true);
                }
            };
        }
        // not confirmed, we display the confirm screen
        return new RpcResolutionWrapper(new ForwardResolution(FRAGMENT_PATH)) {
            @Override
            public Resolution getRpcResolution() {
                return createResolution(false);
            }
        };
    }


}
