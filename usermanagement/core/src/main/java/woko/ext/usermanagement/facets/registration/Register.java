package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import woko.Woko;
import woko.ext.usermanagement.core.*;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Layout;
import woko.mail.MailService;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import java.util.Collections;
import java.util.List;

// @FacetKey(name="register", profileId = "guest") to be assigned to your fallback role(s)
public abstract class Register<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    private static final WLogger logger = WLogger.getLogger(Register.class);

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
        Woko<OsType,UmType,UnsType,FdmType> woko = getWoko();
        DatabaseUserManager<?,?> databaseUserManager = woko.getUserManager();
        if (!(databaseUserManager instanceof RegistrationAwareUserManager)) {
            throw new IllegalStateException("You are using the register facet but your user manager doesn't implement " +
                "RegistrationAwareUserManager (" + databaseUserManager + ")");
        }

        // check that user doesn't already exist
        User u = databaseUserManager.getUserByUsername(username);
        if (u!=null) {
            logger.warn("Attempt to log-in with an already existing username : " + username);
            abc.getValidationErrors().add("facet.username", new LocalizableError("woko.ext.usermanagement.register.ko.username"));
            return getResolution(abc);
        }
        // check that email ain't already taken
        u = databaseUserManager.getUserByEmail(email);
        if (u!=null) {
            logger.warn("Attempt to log-in with an already existing email : " + email);
            abc.getValidationErrors().add("facet.email", new LocalizableError("woko.ext.usermanagement.register.ko.email"));
            return getResolution(abc);
        }

        if (!doValidatePasswords(abc)) {
            return getResolution(abc);
        } else {

            // all good : atually create and register the user
            u = createUser();

            @SuppressWarnings("unchecked")
            RegistrationAwareUserManager<User> registrationAwareUserManager =
                    (RegistrationAwareUserManager<User>)databaseUserManager;
            RegistrationDetails<User> regDetails = registrationAwareUserManager.createRegistration(u);

            // set a session attribute to prevent other users to see this registration !
            getRequest().getSession().setAttribute(SESS_ATTR_WOKO_REGISTERED, true);

            ObjectStore store = getWoko().getObjectStore();
            String regDetailsClassMapping = store.getClassMapping(regDetails.getClass());
            String regDetailsKey = store.getKey(regDetails);

            // send email to freshly registered user if mail service is available and user account is
            // registered
            if (u.getAccountStatus().equals(AccountStatus.Registered)) {
                String mailContent = woko.getLocalizedMessage(getRequest(),
                        "woko.ext.usermanagement.register.mail.content",
                        u.getUsername(),
                        getAppName(),
                        getAppUrl() + "/activate/" + regDetailsClassMapping + "/" + regDetails.getSecretToken());

                MailService mailService = woko.getIoc().getComponent(MailService.KEY);
                if (mailService!=null) {
                    mailService.sendMail(
                            getFromEmailAddress(),
                            u.getEmail(),
                            mailContent);
                } else {
                    logger.error("No email could be sent : no MailService found in IoC.");
                }
            }

            // redirect to postRegister with all params
            return new RedirectResolution("/view/" + regDetailsClassMapping +
                    "/" + regDetailsKey);
        }
    }

    protected abstract String getAppUrl();

    protected String getAppName() {
        Layout layout = getWoko().getFacet(Layout.FACET_NAME, getRequest(), null, Object.class, true);
        return layout.getAppTitle();
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

    protected String getFromEmailAddress() {
        return "noreply@woko.com";
    }



}
