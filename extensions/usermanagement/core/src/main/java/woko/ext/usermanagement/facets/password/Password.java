package woko.ext.usermanagement.facets.password;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.DatabaseUserManager;
import woko.ext.usermanagement.core.User;
import woko.ext.usermanagement.mail.BindingHelper;
import woko.ext.usermanagement.mail.MailTemplatePassword;
import woko.ext.usermanagement.util.PasswordUtil;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Layout;
import woko.ioc.WokoInject;
import woko.mail.MailService;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

@StrictBinding(
        defaultPolicy = StrictBinding.Policy.DENY,
        allow = {
                "facet.currentPassword",
                "facet.newPassword",
                "facet.newPasswordConfirm"
        }
)
@FacetKey(name="password", profileId = "all")
public class Password<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager<?,User>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet{

    private static final WLogger logger = WLogger.getLogger(Password.class);

    public static final String FACET_NAME = "password";

    @Validate(required = true)
    private String currentPassword;
    @Validate(required = true)
    private String newPassword;
    @Validate(required = true)
    private String newPasswordConfirm;

    private MailService mailService;

    @WokoInject(MailService.KEY)
    public void injectMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public String getJspPath() {
        return "/WEB-INF/woko/ext/usermanagement/password.jsp";
    }

    @DontValidate
    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        return new ForwardResolution(getJspPath());
    }

    private User getCurrentUser() {
        String username = getWoko().getUsername(getRequest());
        return getWoko().getUserManager().getUserByUsername(username);
    }

    public Resolution changePassword(ActionBeanContext abc) {
        User u = getCurrentUser();
        // first off verify old password
        DatabaseUserManager<?,User> um = getWoko().getUserManager();
        String encodedPassword = um.encodePassword(currentPassword);
        boolean hasErrors = false;
        if (!encodedPassword.equals(u.getPassword())) {
            abc.getValidationErrors().add("facet.currentPassword", new LocalizableError("woko.ext.usermanagement.password.current.invalid"));
            hasErrors = true;
        }
        // now check new password
        if (!PasswordUtil.validatePasswords(newPassword, newPasswordConfirm)) {
            abc.getValidationErrors().addGlobalError(new LocalizableError("woko.ext.usermanagement.register.ko.passwords"));
            hasErrors = true;
        }
        if (hasErrors) {
            return getResolution(abc);
        }

        // no errors, update the user's password
        u.setPassword(um.encodePassword(newPassword));
        um.save(u);

        // send an email if the mail service is available
        if (mailService!=null) {
            mailService.sendMail(
                    getWoko(),
                    u.getEmail(),
                    getEmailLocale(getRequest()),
                    mailService.getMailTemplate(getTemplateName()),
                    getEmailBinding(u)
            );
        } else {
            logger.warn("No email could be sent : no MailService found in IoC.");
        }

        // redirect to password change confirmation page
        return new RedirectResolution("/passwordConfirm");
    }

    protected Map<String, Object> getEmailBinding(User u) {
        return BindingHelper.newBinding(getWoko(), u, getAppName(), mailService);
    }

    protected String getAppName() {
        Layout layout = getWoko().getFacet(Layout.FACET_NAME, getRequest(), null, Object.class, true);
        return layout.getAppTitle();
    }

    protected Locale getEmailLocale(HttpServletRequest request) {
        return request.getLocale();
    }

    protected String getTemplateName() {
        return MailTemplatePassword.TEMPLATE_NAME;
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        return getCurrentUser()!=null;
    }
}
