package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.*;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

import java.util.Collections;
import java.util.List;

@FacetKey(name="register", profileId = "guest")
public class Register<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FACET_NAME = "register";
    public static final String SESS_ATTR_WOKO_REGISTERED = "wokoRegistered";

    @Validate(required=true)
    private String username;

    @Validate(required=true, converter = EmailTypeConverter.class)
    private String email;

    @Validate(required=true)
    private String password1;

    @Validate(required=true)
    private String password2;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
            abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.usermanagement.register.ko.passwords"));
        }
        return valid;
    }

    public String getJspPath() {
        return "/WEB-INF/woko/ext/usermanagement/register.jsp";
    }

    @DontValidate
    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        return new ForwardResolution(getJspPath());
    }

    public Resolution doRegister(ActionBeanContext abc) {
        // check that no other user with this username already exists
        DatabaseUserManager<?,?> databaseUserManager = getWoko().getUserManager();
        if (!(databaseUserManager instanceof RegistrationAwareUserManager)) {
            throw new IllegalStateException("You are using the register facet but your user manager doesn't implement " +
                "RegistrationAwareUserManager (" + databaseUserManager + ")");
        }

        // chack that user doesn't already exist
        User u = databaseUserManager.getUserByUsername(username);
        if (u!=null) {
            abc.getValidationErrors().add("facet.username", new LocalizableError("woko.ext.usermanagement.register.ko.username"));
            return getResolution(abc);
        }

        // atually create and register the user
        u = createUser();

        @SuppressWarnings("unchecked")
        RegistrationAwareUserManager<User> registrationAwareUserManager =
                (RegistrationAwareUserManager<User>)databaseUserManager;
        RegistrationDetails<User> regDetails = registrationAwareUserManager.createRegistration(u);

        // set a session attribute to prevent other users to see this registration !
        getRequest().getSession().setAttribute(SESS_ATTR_WOKO_REGISTERED, true);

        // redirect to postRegister with all params
        Class<?> regDetailsClass = regDetails.getClass();
        ObjectStore store = getWoko().getObjectStore();
        return new RedirectResolution("/view/" + store.getClassMapping(regDetailsClass) +
                "/" + store.getKey(regDetails));
    }

    protected User createUser() {
        DatabaseUserManager<?,?> databaseUserManager = getWoko().getUserManager();
        if (!(databaseUserManager instanceof RegistrationAwareUserManager)) {
            throw new IllegalStateException("You are using the register facet but your user manager doesn't implement " +
                "RegistrationAwareUserManager (" + databaseUserManager + ")");
        }
        RegistrationAwareUserManager<?> um = (RegistrationAwareUserManager<?>)databaseUserManager;
        return databaseUserManager.createUser(
                username,
                password1,
                email,
                um.getRegisteredUserRoles(),
                um.getRegisteredAccountStatus()
        );
    }

}
