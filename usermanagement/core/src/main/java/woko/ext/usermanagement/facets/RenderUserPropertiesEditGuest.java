package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.all.RenderPropertiesEditImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.ArrayList;
import java.util.List;

@FacetKey(name= WokoFacets.renderPropertiesEdit, profileId = "guest", targetObjectType = User.class)
public class RenderUserPropertiesEditGuest<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends RenderPropertiesEditImpl<OsType,UmType,UnsType,FdmType> {

    @Override
    public List<String> getPropertyNames() {
        List<String> all = super.getPropertyNames();
        ArrayList<String> a = new ArrayList<String>(all);
        a.remove("encodedPassword");
        a.remove("password");
        a.remove("id");
        a.remove("class");
        a.remove("roles");
        a.remove("accountStatus");
        return a;
    }
}
