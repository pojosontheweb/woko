package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.*;
import woko.ext.usermanagement.mail.BindingHelper;
import woko.ext.usermanagement.mail.MailTemplateAccountActivated;
import woko.facets.BaseResolutionFacet;
import woko.facets.builtin.Layout;
import woko.ioc.WokoInject;
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
                "facet.token"
        }
)
@FacetKey(name="activate", profileId="all", targetObjectType = RegistrationDetails.class)
public class ActivateGuest<
        OsType extends ObjectStore,
        UmType extends DatabaseUserManager<?,User>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> implements IInstanceFacet {

    private static final WLogger logger = WLogger.getLogger(ActivateGuest.class);

    @Validate(required=true)
    private String token;

    private MailService mailService;

    @WokoInject(MailService.KEY)
    public void injectMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    protected String getTemplateName() {
        return MailTemplateAccountActivated.TEMPLATE_NAME;
    }

    protected String getAppName() {
        Layout layout = getWoko().getFacet(Layout.FACET_NAME, getRequest(), null, Object.class, true);
        return layout.getAppTitle();
    }

    protected Locale getEmailLocale(HttpServletRequest request) {
        return request.getLocale();
    }

    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        @SuppressWarnings("unchecked")
        RegistrationDetails<User> regDetails = (RegistrationDetails<User>)getFacetContext().getTargetObject();
        ObjectStore store = getWoko().getObjectStore();
        if (token.equals(regDetails.getSecretToken())) {
            User user = regDetails.getUser();
            user.setAccountStatus(AccountStatus.Active);
            getWoko().getUserManager().save(user);

            logger.info("Activated account for user " + user.getUsername());

            // send activation email
           if (mailService != null) {
               MailTemplate template = mailService.getMailTemplate(getTemplateName());
               Map<String, Object> binding = getEmailBinding(user);
               mailService.sendMail(
                       getWoko(),
                       user.getEmail(),
                       getEmailLocale(getRequest()),
                       template,
                       binding);
           } else {
               logger.warn("No email could be sent : no MailService found in IoC.");
           }

            return new RedirectResolution("/activate/" + store.getClassMapping(store.getObjectClass(regDetails))
                + "/" + regDetails.getKey() + "?display");
        } else {
            // TODO better error handling
            throw new RuntimeException("tokens don't match : passed=" + token + ", store=" + regDetails.getSecretToken());
        }
    }

    protected Map<String, Object> getEmailBinding(User u) {
        return BindingHelper.newBinding(getWoko(), u, getAppName(), mailService);
    }

    @DontValidate
    public Resolution display() {
        return new ForwardResolution("/WEB-INF/woko/ext/usermanagement/activate.jsp");
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        return getWoko().getUsername(getRequest())==null;
    }

}
