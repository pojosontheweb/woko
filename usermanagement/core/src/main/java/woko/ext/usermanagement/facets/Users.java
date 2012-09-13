package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name= "users", profileId="usermanager")
public class Users extends ListUsers {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/users.jsp";
    }

}
