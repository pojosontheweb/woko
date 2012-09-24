package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.Edit;
import woko.facets.builtin.developer.EditImpl;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name= Edit.FACET_NAME, profileId = "all", targetObjectType = User.class)
public class EditUserGuest<T extends User,
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager<?,T>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends EditImpl<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        String username = getWoko().getUsername(getRequest());
        // TODO avoid all to be able to view any user !!!
        return targetObject!=null && username==null;
    }

}
