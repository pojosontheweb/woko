package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.Save;
import woko.facets.builtin.developer.SaveImpl;

import java.util.ArrayList;

@FacetKey(name= Save.FACET_NAME, profileId = "usermanager", targetObjectType = User.class)
public class SaveUser extends SaveImpl {

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
