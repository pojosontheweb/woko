package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.BaseForwardResolutionFacet;

@FacetKey(name="postRegister", profileId = "guest")
public class PostRegister extends BaseForwardResolutionFacet {

    private String username;
    private String email;
    private Long userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/postRegister.jsp";
    }

    public String getUserClassName() {
        DatabaseUserManager um = (DatabaseUserManager)getWoko().getUserManager();
        return getWoko().getObjectStore().getClassMapping(um.getUserClass());
    }
}
