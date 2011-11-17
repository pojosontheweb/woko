package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.BaseForwardRpcResolutionFacet;

import java.util.List;

@FacetKey(name="users",profileId="developer")
public class Users extends BaseForwardRpcResolutionFacet {

    private Integer start = null;
    private Integer limit = null;

    private List<User> users;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<User> getUsers() {
        return users;
    }

    public DatabaseUserManager getUserManager() {
        return (DatabaseUserManager)getWoko().getUserManager();
    }

    @Override
    protected Resolution getRpcResolution(ActionBeanContext actionBeanContext) {
        // TODO return JSON list of users
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPath() {
        users = getUserManager().listUsers(start, limit).toList();
        return "/WEB-INF/jsp/woko/usermanagement/users.jsp";
    }
}
