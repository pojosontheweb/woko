package woko.ext.usermanagement.facets.password;

import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.StrictBinding;
import woko.facets.BaseForwardResolutionFacet;

import javax.servlet.http.HttpSession;

@StrictBinding(
        defaultPolicy = StrictBinding.Policy.DENY
)
@FacetKey(name="resetPasswordConfirm", profileId="all")
public class ResetPasswordConfirm extends BaseForwardResolutionFacet implements IInstanceFacet {

    public static final String FACET_NAME = "resetPasswordConfirm";

    private String newPassword = null;

    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public String getPath() {
        HttpSession session = getRequest().getSession();
        session.removeAttribute("wokoResetPasswordToken");
        newPassword = (String)session.getAttribute("wokoNewPassword");
        session.removeAttribute(newPassword);
        return "/WEB-INF/woko/ext/usermanagement/resetPasswordConfirm.jsp";
    }

    @Override
    public boolean matchesTargetObject(Object targetObject) {
        String username = getWoko().getUsername(getRequest());
        return username==null;
    }
}
