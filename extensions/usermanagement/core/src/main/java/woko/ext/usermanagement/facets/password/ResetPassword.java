package woko.ext.usermanagement.facets.password;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.ext.usermanagement.mail.BindingHelper;
import woko.ext.usermanagement.mail.MailTemplateResetPassword;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Layout;
import woko.ioc.WokoInject;
import woko.mail.MailService;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@StrictBinding(
        defaultPolicy = StrictBinding.Policy.DENY,
        allow = {
                "facet.email",
                "facet.token",
                "facet.password1",
                "facet.password2"
        }
)
@FacetKey(name="resetPassword", profileId = "all")
public class ResetPassword<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager<?,User>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    private static final WLogger logger = WLogger.getLogger(ResetPassword.class);

    public static final String FACET_NAME = "resetPassword";

    @Validate(required=true, converter = EmailTypeConverter.class)
    private String email;

    private String token;

    private String password1;

    private String password2;

    private MailService mailService;

    @WokoInject(MailService.KEY)
    public void injectMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public boolean showEmailValidity() {
        return true;
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        return getWoko().getUsername(getRequest())==null;
    }

    @Override
    @DontValidate
    public Resolution getResolution(ActionBeanContext abc) {
        return new ForwardResolution(getJspPath());
    }

    public Resolution emailToken(ActionBeanContext abc) {
        // load user by email
        DatabaseUserManager<?,User> um = getWoko().getUserManager();
        User u = um.getUserByEmail(email);
        if (u!=null) {
            if (mailService!=null) {
                String token = UUID.randomUUID().toString();
                getRequest().getSession().setAttribute("wokoResetPasswordToken", token);
                Map<String,Object> binding = getEmailBinding(u);
                binding.put(
                        MailTemplateResetPassword.RESET_PASSWORD_URL,
                        mailService.getAppUrl() + "/resetPassword?doReset=true&facet.email=" + email + "&facet.token=" + token
                );
                mailService.sendMail(
                        getWoko(),
                        u.getEmail(),
                        getEmailLocale(getRequest()),
                        mailService.getMailTemplate(MailTemplateResetPassword.TEMPLATE_NAME),
                        binding
                );
            } else {
                throw new IllegalStateException("User " + u + " tries to reset its password for email " + email + " but there ain't no MailService ! email = " + email);
            }
            return new RedirectResolution("/resetPassword")
                    .addParameter("confirmEmail", "true")
                    .addParameter("facet.email", email);
        }else {
            if (showEmailValidity()){
                // Email not found and invalid email check process wanted, return on /resetPassword with a global error only
                abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.usermanagement.password.error.email.not.found"));
                return new ForwardResolution("/resetPassword");
            }else{
                return new RedirectResolution("/resetPassword")
                        .addParameter("confirmEmail", "true")
                        .addParameter("facet.email", email);
            }
        }
    }

    protected Map<String, Object> getEmailBinding(User u) {
        return BindingHelper.newBinding(getWoko(), u, getAppName(), mailService);
    }

    public Resolution confirmEmail() {
        return new ForwardResolution("/WEB-INF/woko/ext/usermanagement/resetPasswordConfirmEmail.jsp");
    }

    public Resolution doReset(ActionBeanContext abc) {
        HttpSession session = getRequest().getSession();
        if (token==null) {
            throw new IllegalArgumentException("token must be passed");
        }
        String sessionToken = (String)session.getAttribute("wokoResetPasswordToken");
        if (sessionToken==null) {
            throw new IllegalStateException("session token not found ! cannot reset password for " + email + ", token=" + token);
        }
        if (!sessionToken.equals(token)) {
            throw new IllegalArgumentException("invalid request token " + token + " (not equals to session token " + sessionToken + ")");
        }

        DatabaseUserManager<?,User> um = getWoko().getUserManager();
        User u = um.getUserByEmail(email);

        if (u!=null) {
            // Return to a FORM which will allow user to choose a new password
            return new ForwardResolution("/WEB-INF/woko/ext/usermanagement/setNewPassword.jsp");
        } else {
            throw new IllegalArgumentException("No user found for email " + email);
        }
    }

    public Resolution doSetPassword(ActionBeanContext abc) {
        boolean hasError = false;
        if (password1 == null){
            abc.getValidationErrors().add("facet.password1", new LocalizableError("resetPassword.password1.null"));
            hasError = true;
        }
        if (password2 == null){
            abc.getValidationErrors().add("facet.password2", new LocalizableError("resetPassword.password2.null"));
            hasError = true;
        }
        if ( (!hasError) && (!password1.equals(password2)) ){
            abc.getValidationErrors().addGlobalError(new LocalizableError("resetPassword.passwords.dont.match"));
            hasError = true;
        }
        if (hasError){
            return new ForwardResolution("/WEB-INF/woko/ext/usermanagement/setNewPassword.jsp");
        }else {
            // Everything is ok, set password on login user
            DatabaseUserManager<?,User> um = getWoko().getUserManager();
            User u = um.getUserByEmail(email);
            if (u!=null) {
                u.setPassword(um.encodePassword(password1));
                um.save(u);
            } else {
                throw new IllegalArgumentException("No user found for email " + email);
            }

            return new RedirectResolution("/login?username="+u.getUsername()+"&password="+password1+"&login");
        }

    }

    protected String getAppName() {
        Layout layout = getWoko().getFacet(Layout.FACET_NAME, getRequest(), null, Object.class, true);
        return layout.getAppTitle();
    }

    public String getJspPath() {
        return "/WEB-INF/woko/ext/usermanagement/resetPassword.jsp";
    }

    protected Locale getEmailLocale(HttpServletRequest request) {
        return request.getLocale();
    }

}
