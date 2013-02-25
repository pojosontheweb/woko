package woko.facets.builtin.bootstrap.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.Woko;
import woko.facets.BaseResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpSession;

/**
 * Theme switching facet for the bootstrap skin.
 */
@FacetKey(name="theme", profileId = Woko.ROLE_ALL)
public class SwitchTheme<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends BaseResolutionFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FACET_NAME = "theme";

    /**
     * Constant for storing the theme name
     */
    public static final String WOKO_THEME = "wokoTheme";

    /**
     * Theme to be used.
     */
    private Theme theme;

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

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
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
        abc.getMessages().add(new LocalizableMessage("woko.ext.theming.theme.changed"));
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
        session.setAttribute(WOKO_THEME, theme);
    }

    /**
     * Return the user theme. Default implementation looks up in the
     * http session.
     */
    public Theme getUserTheme() {
        return (Theme)getRequest().getSession().getAttribute(WOKO_THEME);
    }

}
