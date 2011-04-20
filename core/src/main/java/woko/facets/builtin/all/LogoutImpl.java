package woko.facets.builtin.all;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Logout;

@FacetKey(name="logout", profileId="all")
public class LogoutImpl extends BaseResolutionFacet implements Logout {

  public Resolution getResolution() {
    ActionBeanContext abc = getContext();
    abc.getRequest().getSession().invalidate();
    abc.getMessages().add(new SimpleMessage("You have been logged out."));
    return new RedirectResolution("/home");
  }

}
