package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import woko.facets.WokoFacetContext;
import woko.facets.builtin.Delete;

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

  public DeleteImpl() {
    setAcceptNullTargetObject(false);
  }

  public Resolution getResolution() {
    ActionBeanContext abc = getContext();
    if (cancel!=null) {
      WokoFacetContext facetContext = getFacetContext();
      abc.getMessages().add(new SimpleMessage("Cancelled deletion"));
      return new RedirectResolution(
              facetContext.getWoko().facetUrl(
                  "view",
                  facetContext.getTargetObject()));
    }
    if (confirm!=null) {
      WokoFacetContext facetContext = getFacetContext();
      Woko woko = facetContext.getWoko();
      woko.getObjectStore().delete(facetContext.getTargetObject());
      abc.getMessages().add(new SimpleMessage("Object deleted"));
      return new RedirectResolution("/home");
    }
    // not confirmed, we display the confirm screen
    return new ForwardResolution("/WEB-INF/woko/jsp/developer/confirmDelete.jsp");
  }

}
