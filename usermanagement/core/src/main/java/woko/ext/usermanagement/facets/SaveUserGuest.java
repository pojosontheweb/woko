package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.validation.LocalizableError;
import woko.Woko;
import woko.ext.usermanagement.core.AccountStatus;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.builtin.Save;
import woko.facets.builtin.developer.SaveImpl;
import woko.persistence.ObjectStore;

import java.util.Collections;
import java.util.List;

@FacetKey(name= Save.FACET_NAME, profileId = "guest", targetObjectType = User.class)
public class SaveUserGuest extends SaveImpl implements IInstanceFacet {

    @Override
    protected void doSave(ActionBeanContext abc) {
        User user = (User)getFacetContext().getTargetObject();
        user.setRoles(getDefaultRoles());
        user.setAccountStatus(getDefaultAccountStatus());
        // check that no other account exists with this username
        String username = user.getUsername();
        if (username!=null) {
            Woko woko = getFacetContext().getWoko();
            DatabaseUserManager um = (DatabaseUserManager)woko.getUserManager();
            if (um.getUserByUsername(username)!=null) {
                abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.usermanagement.user.register.registration.ko.username.already.taken"));
            } else {
                // username not taken, save the user
                abc.getMessages().add(new LocalizableMessage("woko.ext.usermanagement.user.register.registration.ok.message"));
                woko.getObjectStore().save(user);
            }
        }
    }

    @Override
    protected String getTargetFacetAfterSave() {
        return "postRegister";
    }

    protected List<String> getDefaultRoles() {
        return null;
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        // matches only if there's no logged in user
        Woko woko = getFacetContext().getWoko();
        if (woko.getUsername(getRequest()) != null) {
            return false;
        }
        // and if the object is transient
        User user = (User)targetObject;
        ObjectStore store = woko.getObjectStore();
        String key = store.getKey(user);
        if (key!=null) {
            return false;
        }
        return true;
    }


    public AccountStatus getDefaultAccountStatus() {
        return AccountStatus.Registered;
    }
}
