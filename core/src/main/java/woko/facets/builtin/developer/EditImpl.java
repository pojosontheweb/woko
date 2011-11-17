package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseFacet;
import woko.facets.builtin.Edit;

@FacetKey(name="edit", profileId="developer")
public class EditImpl extends BaseFacet implements Edit {

  public String getFragmentPath() {
    return "/WEB-INF/woko/jsp/developer/edit.jsp";
  }

  public Resolution getResolution(ActionBeanContext abc) {
    return new ForwardResolution(getFragmentPath());
  }



}
