package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import net.sourceforge.stripes.action.StrictBinding;
import woko.ext.usermanagement.core.RegistrationDetails;
import woko.facets.builtin.View;
import woko.facets.builtin.developer.ViewImpl;

@StrictBinding(
        defaultPolicy = StrictBinding.Policy.DENY
)
@FacetKeyList(
        keys= {
            @FacetKey(name= View.FACET_NAME, profileId="all", targetObjectType = RegistrationDetails.class),
            @FacetKey(name= View.FACET_NAME, profileId="guest", targetObjectType = RegistrationDetails.class) // avoids LoginRequired
        }
)
public class ViewRegistrationGuest extends ViewImpl implements IInstanceFacet {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/viewRegistrationGuest.jsp";
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        if (targetObject==null) {
            return false;
        }
        if (getWoko().getUsername(getRequest())!=null) {
            return false;
        }

        RegistrationDetails<?> regDetails = (RegistrationDetails<?>)targetObject;
        String sessionToken = (String)getRequest().getSession().getAttribute(RegisterGuest.SESS_ATTR_WOKO_REGISTERED);
        return sessionToken!=null && sessionToken.equals(regDetails.getSecretToken());
    }
}
