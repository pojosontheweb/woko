package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.all.RenderPropertiesEditImpl;

@FacetKey(name= WokoFacets.renderPropertiesEdit, profileId = "all", targetObjectType = User.class)
public class EditUserPropertiesGuest extends RenderPropertiesEditImpl {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/renderPropertiesEditUserGuest.jsp";
    }
}
