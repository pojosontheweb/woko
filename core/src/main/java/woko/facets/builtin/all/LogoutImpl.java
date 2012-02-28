package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Logout;
import woko.facets.builtin.WokoFacets;

@FacetKey(name= WokoFacets.logout, profileId="all")
public class LogoutImpl extends BaseResolutionFacet implements Logout {

  public Resolution getResolution(ActionBeanContext abc) {
    abc.getRequest().getSession().invalidate();
    abc.getMessages().add(new LocalizableMessage("woko.logout.success"));
    return new RedirectResolution("/home");
  }

}
