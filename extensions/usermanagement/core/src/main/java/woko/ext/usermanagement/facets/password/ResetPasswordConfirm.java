package woko.ext.usermanagement.facets.password;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.StrictBinding;
import woko.ext.usermanagement.core.RegistrationAwareUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.BaseForwardResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpSession;

@StrictBinding(
        defaultPolicy = StrictBinding.Policy.DENY,
        allow = {"facet.newPassword"}
)
@FacetKey(name="resetPasswordConfirm", profileId="all")
public class ResetPasswordConfirm<
        OsType extends ObjectStore,
        UmType extends RegistrationAwareUserManager<User>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseForwardResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    public static final String FACET_NAME = "resetPasswordConfirm";

    private String newPassword = null;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/resetPasswordConfirm.jsp";
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        String username = getWoko().getUsername(getRequest());
        return username==null;
    }
}
