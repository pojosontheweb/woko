package woko.ext.usermanagement.facets.password;

import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.facets.BaseForwardResolutionFacet;

@FacetKey(name="passwordConfirm", profileId="all")
public class PasswordConfirm extends BaseForwardResolutionFacet implements IInstanceFacet {

    public static final String FACET_NAME = "passwordConfirm";

    @Override
    public String getPath() {
        return "/WEB-INF/woko/ext/usermanagement/passwordConfirm.jsp";
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        String username = getWoko().getUsername(getRequest());
        return username!=null;
    }
}
