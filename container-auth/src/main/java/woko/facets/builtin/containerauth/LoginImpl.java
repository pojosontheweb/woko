package woko.facets.builtin.containerauth;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Login;

@FacetKey(name="login", profileId="all")
public class LoginImpl extends BaseResolutionFacet implements Login {

  public Resolution getResolution(ActionBeanContext abc) {
    abc.getMessages().add(new SimpleMessage("You have been logged in"));
    return new RedirectResolution("/home");
  }


}
