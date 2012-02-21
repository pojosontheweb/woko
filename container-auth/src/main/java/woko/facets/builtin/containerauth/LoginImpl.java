package woko.facets.builtin.containerauth;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Login;
import woko.facets.builtin.WokoFacets;

@FacetKey(name= WokoFacets.login, profileId="all")
public class LoginImpl extends BaseResolutionFacet implements Login {

  public Resolution getResolution(ActionBeanContext abc) {
    abc.getMessages().add(new LocalizableMessage("stripes.login.success"));
    return new RedirectResolution("/home");
  }


}
