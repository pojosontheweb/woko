package woko.ext.usermanagement.facets.password;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.ResetPasswordDetails;
import woko.ext.usermanagement.core.RegistrationAwareUserManager;
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
import java.util.Random;

@StrictBinding(
        defaultPolicy = StrictBinding.Policy.DENY,
        allow = {
                "facet.email",
                "facet.token"
        }
)
@FacetKey(name="resetPassword", profileId = "all")
public class ResetPassword<
        OsType extends ObjectStore,
        UmType extends RegistrationAwareUserManager<User>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    private static final WLogger logger = WLogger.getLogger(ResetPassword.class);

    public static final String FACET_NAME = "resetPassword";

    @Validate(required=true, converter = EmailTypeConverter.class)
    private String email;

    private String token;

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
        RegistrationAwareUserManager<User> um = getWoko().getUserManager();
        User u = um.getUserByEmail(email);
        if (u!=null) {
            if (mailService!=null) {
                // #208
                ResetPasswordDetails resetDetails = um.createPasswordResetDetails(u);
                Map<String,Object> binding = getEmailBinding(u);
                binding.put(
                        MailTemplateResetPassword.RESET_PASSWORD_URL,
                        mailService.getAppUrl() + "/resetPassword?doReset=true&facet.email=" + email +
                                "&facet.token=" + resetDetails.getKey()
                );
                mailService.sendMail(
                        getWoko(),
                        u.getEmail(),
                        getEmailLocale(getRequest()),
                        mailService.getMailTemplate(MailTemplateResetPassword.TEMPLATE_NAME),
                        binding
                );
            } else {
                throw new IllegalStateException("User " + u + " tries to reset its password for email " + email +
                        " but there ain't no MailService ! email = " + email);
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
        if (token==null) {
            throw new IllegalArgumentException("token must be passed");
        }
        if (email==null) {
            throw new IllegalArgumentException("email must be passed");
        }
        RegistrationAwareUserManager<User> um = getWoko().getUserManager();
        ResetPasswordDetails passwordResetDetails = um.getPasswordResetDetails(token);
        if (passwordResetDetails==null) {
            throw new IllegalArgumentException("Could not find email details for " + email +
                ", token=" + token);
        }
        if (!passwordResetDetails.getEmail().equals(email)) {
            throw new IllegalStateException("Email don't match password reset details ! " +
                email + "!=" +passwordResetDetails.getEmail() + ", token=" + token);
        }
        // compute time difference in days
        long diff = System.currentTimeMillis() - passwordResetDetails.getCreationDate().getTime();
        long days = diff / (24 * 3600 * 1000); // TODO might not work with TimeZones...
        if (days>30) {
            throw new IllegalStateException("More than 30 days passed, unable to reset password " +
                " for " + email + ", token=" + token);
        }

        User u = um.getUserByEmail(email);
        if (u!=null) {
            String newPassword = generatePassword();
            u.setPassword(um.encodePassword(newPassword));
            um.save(u);
            um.passwordHasBeenReset(token);
            logger.warn("password reset for email " + email);
            return new RedirectResolution("/resetPasswordConfirm")
                    .addParameter("facet.newPassword", newPassword);
        } else {
            throw new IllegalArgumentException("No user found for email " + email);
        }
    }

    protected String generatePassword() {
        return RandomString.getRandomString(10);
    }

    protected String getAppName() {
        Layout layout = getWoko().getFacet(Layout.FACET_NAME, getRequest(), null, Object.class, true);
        return layout.getAppTitle();
    }

    public String getJspPath() {
        return "/WEB-INF/woko/ext/usermanagement/resetPassword.jsp";
    }
    protected String getTemplateName() {
        return MailTemplateResetPassword.TEMPLATE_NAME;
    }


    protected Locale getEmailLocale(HttpServletRequest request) {
        return request.getLocale();
    }

    private static class RandomString {

        private static final String charset = "!@#$%^&*()" +
            "0123456789" +
            "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static String getRandomString(int length) {
            Random rand = new Random(System.currentTimeMillis());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i <= length; i++ ) {
                int pos = rand.nextInt(charset.length());
                sb.append(charset.charAt(pos));
            }
            return sb.toString();
        }

    }

}
