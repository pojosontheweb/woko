package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.BaseForwardResolutionFacet;

@FacetKey(name="postRegister", profileId = "guest", targetObjectType = User.class)
public class PostRegister extends BaseForwardResolutionFacet {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/postRegister.jsp";
    }

    public String getUserClassName() {
        DatabaseUserManager um = (DatabaseUserManager)getWoko().getUserManager();
        return getWoko().getObjectStore().getClassMapping(um.getUserClass());
    }
}
