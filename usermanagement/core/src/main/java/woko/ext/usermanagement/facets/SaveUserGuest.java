package woko.ext.usermanagement.facets;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import woko.Woko;
import woko.ext.usermanagement.core.*;
import woko.facets.builtin.Save;
import woko.facets.builtin.developer.SaveImpl;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.List;

@FacetKey(name= Save.FACET_NAME, profileId = "guest", targetObjectType = User.class)
public class SaveUserGuest<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends SaveImpl<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    private String password1;
    private String password2;

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    private boolean doValidatePasswords(ActionBeanContext abc) {
        // TODO more validation (length, strength etc.)
        boolean valid = true;
        if (password1==null) {
            valid = false;
        }
        if (valid && password2==null) {
            valid = false;
        }
        if (valid) {
            valid = password1.equals(password2);
        }
        if (!valid) {
            abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.usermanagement.user.register.registration.ko.passwords.invalid"));
        }
        return valid;
    }

    @Override
    protected void doSave(ActionBeanContext abc) {
        User user = (User)getFacetContext().getTargetObject();
        user.setRoles(getDefaultRoles());
        user.setAccountStatus(getDefaultAccountStatus());
        // check that no other account exists with this username
        String username = user.getUsername();
        if (username!=null) {
            Woko<OsType,UmType,UnsType,FdmType> woko = getFacetContext().getWoko();
            DatabaseUserManager um = woko.getUserManager();
            if (um.getUserByUsername(username)!=null) {
                abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.usermanagement.user.register.registration.ko.username.already.taken"));
            } else {
                // username not taken
                // check that passwords match
                if (doValidatePasswords(abc)) {
                    // passwords match, all good...
                    // register user if user manager supports it
                    if (um instanceof RegistrationAwareUserManager) {
                        @SuppressWarnings("unchecked")
                        RegistrationAwareUserManager<User> registrationAwareUserManager =
                                (RegistrationAwareUserManager<User>)um;
                        RegistrationDetails<User> registration = registrationAwareUserManager.createRegistration(user);
                        // TODO send email to user with registration details
                        System.out.println(registration);

                    }
                    // save the user
                    abc.getMessages().add(new LocalizableMessage("woko.ext.usermanagement.user.register.registration.ok.message"));
                    woko.getObjectStore().save(user);
                }
            }
        }
    }

    @Override
    protected Resolution getNonRpcResolution(ActionBeanContext abc) {
        User user = (User)getFacetContext().getTargetObject();
        ObjectStore os = getWoko().getObjectStore();
        return new RedirectResolution("/postRegister")
                .addParameter("facet.username", user.getUsername())
                .addParameter("facet.email", user.getEmail())
                .addParameter("facet.userId", os.getKey(user));
    }

    protected List<String> getDefaultRoles() {
        return null;
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        // matches only if there's no logged in user
        Woko<OsType,UmType,UnsType,FdmType> woko = getFacetContext().getWoko();
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
