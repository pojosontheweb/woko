package woko.ext.usermanagement.facets.password;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Layout;
import woko.mail.MailService;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import javax.servlet.http.HttpSession;
import java.util.UUID;

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
        UmType extends DatabaseUserManager<?,User>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    private static final WLogger logger = WLogger.getLogger(ResetPassword.class);

    public static final String FACET_NAME = "resetPassword";

    @Validate(required=true, converter = EmailTypeConverter.class)
    private String email;

    private String token;

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
            MailService mailService = getWoko().getIoc().getComponent(MailService.KEY);
            if (mailService!=null) {
                String token = UUID.randomUUID().toString();
                getRequest().getSession().setAttribute("wokoResetPasswordToken", token);
                // sent email with link containing the reset token
                mailService.sendMail(
                        u.getEmail(),
                        getWoko().getLocalizedMessage(
                                getRequest(),
                                "woko.ext.usermanagement.password.reset.email.subject",
                                getAppName()
                        ),
                        getWoko().getLocalizedMessage(
                                getRequest(),
                                "woko.ext.usermanagement.password.reset.email.content",
                                getAppName(),
                                mailService.getAppUrl() + "/resetPassword?doReset=true&facet.email=" + email + "&facet.token=" + token
                        )
                );
            } else {
                throw new IllegalStateException("User tries to reset its password but there ain't no MailService ! email = " + email);
            }
        }
        return new RedirectResolution("/resetPassword")
                .addParameter("confirmEmail", "true")
                .addParameter("facet.email", email);
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
            throw new IllegalStateException("session token not found ! cannot reset password");
        }
        if (!sessionToken.equals(token)) {
            throw new IllegalArgumentException("invalid request token (not equals to session token)");
        }

        DatabaseUserManager<?,User> um = getWoko().getUserManager();
        User u = um.getUserByEmail(email);

        if (u!=null) {
            String newPassword = generatePassword();
            session.setAttribute("wokoNewPassword", newPassword);
            u.setPassword(um.encodePassword(newPassword));
            um.save(u);
            logger.warn("password reset for email " + email);
            return new RedirectResolution("/resetPasswordConfirm");
        } else {
            throw new IllegalArgumentException("No user found for email " + email);
        }
    }

    protected String generatePassword() {
        return UUID.randomUUID().toString();
    }

    protected String getAppName() {
        Layout layout = getWoko().getFacet(Layout.FACET_NAME, getRequest(), null, Object.class, true);
        return layout.getAppTitle();
    }

    public String getJspPath() {
        return "/WEB-INF/woko/ext/usermanagement/resetPassword.jsp";
    }
}
