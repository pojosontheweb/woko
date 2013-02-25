package woko.facets.builtin.bootstrap.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.*;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpSession;

/**
 * Theme switching facet for the bootstrap skin.
 */
@StrictBinding(
        allow = {"facet.alternativeLayout", "facet.sourcePage"}
)
@FacetKey(name="alternativeLayout", profileId = Woko.ROLE_ALL)
public class SwitchLayout<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FACET_NAME = "alternativeLayout";

    /**
     * Constant for storing the theme name
     */
    public static final String WOKO_ALTERNATIVE_LAYOUT = "wokoAlternativeLayout";

    /**
     * Theme to be used.
     */
    private AlternativeLayout alternativeLayout;

    /**
     * Optional source page to redirect to after the theme
     * has been switched. If no value is supplied
     * will redirect to "/home"
     */
    private String sourcePage;

    public String getSourcePage() {
        return sourcePage;
    }

    public void setSourcePage(String sourcePage) {
        this.sourcePage = sourcePage;
    }

    public AlternativeLayout getAlternativeLayout() {
        return alternativeLayout;
    }

    public void setAlternativeLayout(AlternativeLayout alternativeLayout) {
        this.alternativeLayout = alternativeLayout;
    }

    /**
     * Change the theme for the current user and redirect to
     * sourcePage or "/home"
     */
    @Override
    public Resolution getResolution(ActionBeanContext abc) {
        doSwitch();
        String source = this.sourcePage;
        if (source==null) {
            source = "/home";
        }
        //abc.getMessages().add(new LocalizableMessage("woko.ext.theming.layout.changed"));
        return new RedirectResolution(source);
    }

    /**
     * Template method that actually switches the theme. Default
     * implementation uses the HttpSession.
     * Subclasses can use different methods in order to store the
     * user pref in the user object or as a cookie.
     */
    protected void doSwitch() {
        HttpSession session = getRequest().getSession();
        session.setAttribute(WOKO_ALTERNATIVE_LAYOUT, alternativeLayout);
    }

    /**
     * Return the user theme. Default implementation looks up in the
     * http session.
     */
    public AlternativeLayout getUserAlternativeLayout() {
        return (AlternativeLayout)getRequest().getSession().getAttribute(WOKO_ALTERNATIVE_LAYOUT);
    }

}
