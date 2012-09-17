package woko.ext.usermanagement.facets.usermanager;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.Save;
import woko.facets.builtin.developer.SaveImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.ArrayList;

@FacetKey(name= Save.FACET_NAME, profileId = "usermanager", targetObjectType = User.class)
public class SaveUser<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends SaveImpl<OsType,UmType,UnsType,FdmType> {

    private String roles;

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    protected void doSave(ActionBeanContext abc) {
        String[] parts = roles!=null ? roles.split(",") : new String[0];
        ArrayList<String> newRoles = new ArrayList<String>();
        for (String part:parts) {
            newRoles.add(part.trim());
        }
        User user = (User)getFacetContext().getTargetObject();
        user.setRoles(newRoles);
        super.doSave(abc);
    }
}
