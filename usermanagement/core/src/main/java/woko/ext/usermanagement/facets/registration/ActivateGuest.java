package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import woko.ext.usermanagement.core.*;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UsernameResolutionStrategy;

public class ActivateGuest<
        OsType extends ObjectStore,
        UmType extends RegistrationAwareUserManager<User>,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    @Validate(required=true)
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
            return new RedirectResolution("/activate/" + store.getClassMapping(regDetails.getClass())
                + "/" + regDetails.getKey() + "?display");
        } else {
            // TODO better error handling
            throw new RuntimeException("tokens don't match : passed=" + token + ", store=" + regDetails.getSecretToken());
        }
    }

    @DontValidate
    public Resolution display(ActionBeanContext abc) {
        return new ForwardResolution("/WEB-INF/woko/ext/usermanagement/activate.jsp");
    }
}
