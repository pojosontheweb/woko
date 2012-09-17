package woko.ext.usermanagement.facets.registration;

import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.ext.usermanagement.core.RegistrationDetails;
import woko.facets.builtin.View;
import woko.facets.builtin.developer.ViewImpl;

@FacetKey(name= View.FACET_NAME, profileId="guest", targetObjectType = RegistrationDetails.class)
public class ViewRegistrationGuest extends ViewImpl implements IInstanceFacet {

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/viewRegistrationGuest.jsp";
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        return super.matchesTargetObject(targetObject) &&
                getRequest().getSession().getAttribute(Register.SESS_ATTR_WOKO_REGISTERED)!=null;
    }
}
