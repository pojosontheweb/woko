package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.rpc.RpcResolutionWrapper;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Delete;
import woko.facets.builtin.Json;

@FacetKey(name="delete", profileId="developer")
public class DeleteImpl extends BaseResolutionFacet implements Delete {

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

  public Resolution getResolution(final ActionBeanContext abc) {
    if (cancel!=null) {
      WokoFacetContext facetContext = getFacetContext();
      abc.getMessages().add(new LocalizableMessage("woko.devel.delete.cancel"));
      return new RedirectResolution(
              facetContext.getWoko().facetUrl(
                  "view",
                  facetContext.getTargetObject()));
    }
    if (confirm!=null) {
      final WokoFacetContext facetContext = getFacetContext();
      final Woko woko = facetContext.getWoko();
      final Object targetObject = facetContext.getTargetObject();
      woko.getObjectStore().delete(targetObject);
      abc.getMessages().add(new LocalizableMessage("woko.devel.delete.confirm"));
      return new RpcResolutionWrapper(new RedirectResolution("/home")) {
          @Override
          public Resolution getRpcResolution() {
              return new StreamingResolution("text/json", "{ \"success\": true }");
          }
      };
    }
    // not confirmed, we display the confirm screen
    return new ForwardResolution("/WEB-INF/woko/jsp/developer/confirmDelete.jsp");
  }

}
