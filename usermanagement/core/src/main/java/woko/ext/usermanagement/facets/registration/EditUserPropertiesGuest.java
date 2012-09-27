package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.all.RenderPropertiesEditImpl;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

import java.util.ArrayList;
import java.util.List;

@FacetKey(name= WokoFacets.renderPropertiesEdit, profileId = "all", targetObjectType = User.class)
public class EditUserPropertiesGuest<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager<?,?>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertiesEditImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public boolean isPartialForm() {
        return true;
    }

    @Override
    public List<String> getPropertyNames() {
        List<String> all = new ArrayList<String>(super.getPropertyNames());
        all.remove("accountStatus");
        all.remove("class");
        all.remove("email");
        all.remove("id");
        all.remove("password");
        all.remove("roles");
        all.remove("username");
        return all;
    }

    @Override
    public String getFieldPrefix() {
        return "facet.user";
    }
}
