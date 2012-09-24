package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import woko.Woko;
import woko.ext.usermanagement.core.*;
import woko.ext.usermanagement.util.PasswordUtil;
import woko.facets.builtin.Edit;
import woko.facets.builtin.Layout;
import woko.facets.builtin.Save;
import woko.facets.builtin.WokoFacets;
import woko.facets.builtin.developer.SaveImpl;
import woko.mail.MailService;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;
import woko.util.LinkUtil;
import woko.util.WLogger;

@FacetKey(name= Save.FACET_NAME, profileId = "all", targetObjectType = User.class)
public class SaveUserGuest<T extends User,
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager<?,T>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends SaveImpl<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    private static final WLogger logger = WLogger.getLogger(SaveUserGuest.class);

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

    private RegistrationDetails<T> registrationDetails;

    @DontValidate
    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        return super.getResolution(abc);
    }

    public Resolution save(ActionBeanContext abc) {
        return super.getResolution(abc);
    }

    @Override
    protected void doSave(ActionBeanContext abc) {
                // check that no other user with this username already exists
        Woko<OsType,UmType,UnsType,FdmType> woko = getWoko();
        UmType userManager = woko.getUserManager();
        if (!(userManager instanceof RegistrationAwareUserManager)) {
            throw new IllegalStateException("You are using the register facet but your user manager doesn't implement " +
                "RegistrationAwareUserManager (" + userManager + ")");
        }

        // check that user doesn't already exist
        if (userManager.getUserByUsername(username)!=null) {
            logger.warn("Attempt to register with an already existing username : " + username);
            abc.getValidationErrors().add("facet.username", new LocalizableError("woko.ext.usermanagement.register.ko.username"));
            return;
        }
        // check that email ain't already taken
        if (userManager.getUserByEmail(email)!=null) {
            logger.warn("Attempt to register with an already existing email : " + email);
            abc.getValidationErrors().add("facet.email", new LocalizableError("woko.ext.usermanagement.register.ko.email"));
            return;
        }

        if (!PasswordUtil.validatePasswords(password1, password2)) {
            abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.usermanagement.register.ko.passwords"));
            return;
        }

        @SuppressWarnings("unchecked")
        T user = (T)getFacetContext().getTargetObject();

        @SuppressWarnings("unchecked")
        RegistrationAwareUserManager<T> registrationAwareUserManager =
                (RegistrationAwareUserManager<T>)userManager;
        registrationDetails = registrationAwareUserManager.createRegistration(user);

        OsType store = getWoko().getObjectStore();
        String regDetailsClassMapping = store.getClassMapping(registrationDetails.getClass());

        // send email to freshly registered user if mail service is available and user account is
        // registered
        if (user.getAccountStatus().equals(AccountStatus.Registered)) {
            MailService mailService = woko.getIoc().getComponent(MailService.KEY);
            if (mailService!=null) {
                mailService.sendMail(
                        user.getEmail(),
                        woko.getLocalizedMessage(getRequest(),
                            "woko.ext.usermanagement.register.mail.content",
                            user.getUsername(),
                            getAppName(),
                            mailService.getAppUrl() + "/activate/" + regDetailsClassMapping + "/" + registrationDetails.getKey() +
                                "?facet.token=" + registrationDetails.getSecretToken()));
            } else {
                logger.warn("No email could be sent to " + email + ", : no MailService found in IoC.");
            }
        }
        super.doSave(abc);  // save the user
        store.save(registrationDetails); // save the reg details
    }

    @Override
    protected Resolution getNonRpcResolution(ActionBeanContext abc) {
        String link = "/" + LinkUtil.getUrl(getWoko(), registrationDetails, "view");
        return new RedirectResolution(link);
    }

    protected String getAppName() {
        Layout layout = getWoko().getFacet(Layout.FACET_NAME, getRequest(), null, Object.class, true);
        return layout.getAppTitle();
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        String username = getWoko().getUsername(getRequest());
        return targetObject!=null && username==null;
    }
}
