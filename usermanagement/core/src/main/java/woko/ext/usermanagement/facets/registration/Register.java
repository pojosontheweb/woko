package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import woko.Woko;
import woko.ext.usermanagement.core.*;
import woko.ext.usermanagement.mail.BindingHelper;
import woko.ext.usermanagement.mail.MailTemplateRegister;
import woko.ext.usermanagement.util.PasswordUtil;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Layout;
import woko.facets.builtin.WokoFacets;
import woko.mail.MailService;
import woko.mail.MailTemplate;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

@StrictBinding(
        defaultPolicy = StrictBinding.Policy.DENY,
        allow = {
                "facet.username",
                "facet.email",
                "facet.password1",
                "facet.password2",
                "facet.user.*"
        },
        deny = {
                "facet.user.username",
                "facet.user.password",
                "facet.user.roles",
                "facet.user.accountStatus"
        }
)
@FacetKey(name="register", profileId = "all")
public class Register<T extends User,
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager<?,T>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

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

    private T user;

    public T getUser() {
        return user;
    }

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

    public String getJspPath() {
        return "/WEB-INF/woko/ext/usermanagement/register.jsp";
    }

    // TODO remove when we have @Before methods ! used to create the user before binding/validation
    @Override
    public void setFacetContext(IFacetContext iFacetContext) {
        super.setFacetContext(iFacetContext);
        user = createTransientUser();
    }

    protected T createTransientUser() {
        DatabaseUserManager<?,T> um = getWoko().getUserManager();
        Class<? extends T> clazz = um.getUserClass();
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DontValidate
    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        return new ForwardResolution(getJspPath());
    }

    protected Locale getEmailLocale(HttpServletRequest request) {
        return request.getLocale();
    }

    public Resolution doRegister(ActionBeanContext abc) {

        // verify captcha if needed
        if (isUseCaptcha()) {
            HttpServletRequest request = getRequest();
            String remoteAddr = request.getRemoteAddr();
            ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
            reCaptcha.setPrivateKey(getReCaptchaPrivateKey());
            String challenge = request.getParameter("recaptcha_challenge_field");
            String uresponse = request.getParameter("recaptcha_response_field");
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
            if (!reCaptchaResponse.isValid()) {
                logger.warn("Recaptcha failure, username:" + username);
                abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.usermanagement.register.ko.captcha"));
                return getResolution(abc);
            }
        }

        // check that no other user with this username already exists
        Woko<OsType, UmType, UnsType, FdmType> woko = getWoko();
        DatabaseUserManager<?, T> databaseUserManager = woko.getUserManager();
        if (!(databaseUserManager instanceof RegistrationAwareUserManager)) {
            throw new IllegalStateException("You are using the register facet but your user manager doesn't implement " +
                    "RegistrationAwareUserManager (" + databaseUserManager + ")");
        }

        // check that user doesn't already exist
        User u = databaseUserManager.getUserByUsername(username);
        if (u != null) {
            logger.warn("Attempt to register with an already existing username : " + username);
            abc.getValidationErrors().add("facet.username", new LocalizableError("woko.ext.usermanagement.register.ko.username"));
            return getResolution(abc);
        }
        // check that email ain't already taken
        u = databaseUserManager.getUserByEmail(email);
        if (u != null) {
            logger.warn("Attempt to register with an already existing email : " + email);
            abc.getValidationErrors().add("facet.email", new LocalizableError("woko.ext.usermanagement.register.ko.email"));
            return getResolution(abc);
        }

        if (!PasswordUtil.validatePasswords(password1, password2)) {
            abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.usermanagement.register.ko.passwords"));
            return getResolution(abc);
        } else {

            @SuppressWarnings("unchecked")
            RegistrationAwareUserManager<T> registrationAwareUserManager =
                    (RegistrationAwareUserManager<T>) databaseUserManager;
            user.setAccountStatus(registrationAwareUserManager.getRegisteredAccountStatus());
            user.setRoles(registrationAwareUserManager.getRegisteredRoles());
            user.setUsername(username);
            user.setPassword(databaseUserManager.encodePassword(password1));
            user.setEmail(email);

            // use validate facet in order to check the user's validation constraints
            woko.facets.builtin.Validate validateFacet = woko.getFacet(WokoFacets.validate, abc.getRequest(), user, user.getClass());
            if (validateFacet != null) {
                logger.debug("Validation facet found, validating before saving...");
                if (!validateFacet.validate(abc)) {
                    // validation issue : forward
                    return getResolution(abc);
                }
            }

            // all good, save user
            databaseUserManager.save(user);

            // and create registration
            @SuppressWarnings("unchecked")
            RegistrationDetails<T> regDetails = registrationAwareUserManager.createRegistration(user);
            OsType store = getWoko().getObjectStore();
            String regDetailsClassMapping = store.getClassMapping(regDetails.getClass());
            String regDetailsKey = store.getKey(regDetails);

            // set a session attribute to prevent other users to see this registration !
            getRequest().getSession().setAttribute(SESS_ATTR_WOKO_REGISTERED, regDetails.getSecretToken());

            // send email to freshly registered user if mail service is available and user account is
            // registered
            if (user.getAccountStatus().equals(AccountStatus.Registered)) {
                MailService mailService = woko.getIoc().getComponent(MailService.KEY);
                if (mailService != null) {
                    MailTemplate template = mailService.getMailTemplate(getTemplateName());
                    Map<String, Object> binding = BindingHelper.newBinding(user, getAppName(), mailService);
                    binding.put(
                            MailTemplateRegister.REGISTER_URL,
                            mailService.getAppUrl() + "/activate/" +
                                    regDetailsClassMapping + "/" +
                                    regDetails.getKey() + "?facet.token=" +
                                    regDetails.getSecretToken()
                    );
                    mailService.sendMail(
                            woko,
                            user.getEmail(),
                            getEmailLocale(getRequest()),
                            template,
                            binding);
                } else {
                    logger.warn("No email could be sent : no MailService found in IoC.");
                }
            }

            // redirect to postRegister with all params
            return new RedirectResolution("/view/" + regDetailsClassMapping +
                    "/" + regDetailsKey);
        }
    }

    protected String getAppName() {
        Layout layout = getWoko().getFacet(Layout.FACET_NAME, getRequest(), null, Object.class, true);
        return layout.getAppTitle();
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        return getWoko().getUsername(getRequest())==null;
    }

    protected String getTemplateName() {
        return MailTemplateRegister.TEMPLATE_NAME;
    }

    public boolean isUseCaptcha() {
        return false;
    }

    public String getReCaptchaPublicKey() {
        return null;
    }

    public String getReCaptchaPrivateKey() {
        return null;
    }

}
