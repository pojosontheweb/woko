package woko.facets.builtin.developer;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseFacet;
import woko.facets.builtin.Edit;
import woko.facets.builtin.WokoFacets;

@FacetKey(name = WokoFacets.edit, profileId = "developer")
public class EditImpl extends BaseFacet implements Edit {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/edit.jsp";

    public String getFragmentPath() {
        return FRAGMENT_PATH;
    }

    public Resolution getResolution(ActionBeanContext abc) {
        return new ForwardResolution(getFragmentPath());
    }


}
