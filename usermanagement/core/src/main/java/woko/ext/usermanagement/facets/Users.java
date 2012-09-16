package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name= "users", profileId="usermanager")
public class Users<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends ListUsers<OsType,UmType,UnsType,FdmType> {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/users.jsp";
    }

}
