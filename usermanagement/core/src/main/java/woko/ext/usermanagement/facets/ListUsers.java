package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.ListObjects;
import woko.facets.builtin.developer.ListTabularImpl;
import woko.persistence.ObjectStore;
import woko.persistence.ResultIterator;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FacetKey(name= ListObjects.FACET_NAME, profileId = "usermanager", targetObjectType = User.class)
public class ListUsers<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends ListTabularImpl<OsType,UmType,UnsType,FdmType> {

    public static final List<String> PROP_NAMES;

    static {
        ArrayList<String> a = new ArrayList<String>();
        a.add("id");
        a.add("username");
        a.add("roles");
        a.add("accountStatus");
        a.add("class");
        PROP_NAMES = Collections.unmodifiableList(a);
    }

    @Override
    public List<String> getPropertyNames() {
        return PROP_NAMES;
    }

    @Override
    protected ResultIterator<?> createResultIterator(ActionBeanContext abc, int start, int limit) {
        DatabaseUserManager dbUm = getWoko().getUserManager();
        return dbUm.listUsers(start, limit);
    }
}
