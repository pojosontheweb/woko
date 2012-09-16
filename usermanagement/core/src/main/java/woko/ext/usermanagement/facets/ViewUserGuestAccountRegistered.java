package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.Woko;
import woko.ext.usermanagement.core.AccountStatus;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.View;
import woko.facets.builtin.developer.ViewImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name= View.FACET_NAME,profileId = "guest", targetObjectType = User.class)
public class ViewUserGuestAccountRegistered<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends ViewImpl implements IInstanceFacet {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/viewAccountGuestRegistered.jsp";
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        if (!super.matchesTargetObject(targetObject)) {
            return false;
        }
        // matches only if there's no logged in user
        Woko woko = getFacetContext().getWoko();
        if (woko.getUsername(getRequest()) != null) {
            return false;
        }
        // and if the account is just registered
        User user = (User)targetObject;
        if (!user.getAccountStatus().equals(AccountStatus.Registered)) {
            return false;
        }
        return true;
    }

}
