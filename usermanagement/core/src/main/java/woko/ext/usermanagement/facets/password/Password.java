package woko.ext.usermanagement.facets.password;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

@FacetKey(name="password", profileId = "all", targetObjectType = User.class)
public class Password<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet{

    @Validate(required = true)
    private String currentPassword;
    @Validate(required = true)
    private String newPassword;
    @Validate(required = true)
    private String newPasswordConfirm;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public String getJspPath() {
        return "/WEB-INF/woko/ext/usermanagement/password.jsp";
    }

    @DontValidate
    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        return new ForwardResolution(getJspPath());
    }

    public Resolution changePassword() {
        User u = (User)getFacetContext().getTargetObject();

    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        User u = (User)targetObject;
        if (u!=null) {
            String username = u.getUsername();
            if (username==null) {
                return false;
            }
            String currentUsername = getWoko().getUsername(getRequest());
            return currentUsername!=null && currentUsername.equals(u.getUsername());
        }
        return false;
    }
}
