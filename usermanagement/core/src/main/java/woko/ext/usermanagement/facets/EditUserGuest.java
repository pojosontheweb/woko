package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.developer.EditImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name="edit", profileId = "guest", targetObjectType = User.class)
public class EditUserGuest<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends EditImpl<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        // matches only if there's no logged in user
        Woko<OsType,UmType,UnsType,FdmType> woko = getFacetContext().getWoko();
        return woko.getUsername(getRequest()) == null;
    }


}
