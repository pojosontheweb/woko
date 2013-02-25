package woko.facets.builtin.developer;


import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import woko.facets.BaseFragmentFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.HashMap;
import java.util.Map;

@FacetKey(name = "theme", profileId = "developer")
public class Theme<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
> extends BaseFragmentFacet<OsType,UmType,UnsType,FdmType> {

    public static final String FRAGMENT_PATH = "/WEB-INF/woko/jsp/developer/theme.jsp";
    public static final String THEME_COOKIE = "themeName";

    String themeName;

    public String getPath() {
        return FRAGMENT_PATH;
    }

    public Map<String,String> getAvailableThemes(){
        Map<String, String> ret = new HashMap<String, String>();

        ret.put("Bootstrap", "bootstrap-v2.3.0");
        ret.put("Amelia", "amelia");
        ret.put("Cerulean", "cerulean");
        ret.put("Cosmo", "cosmo");
        ret.put("Cyborg", "cyborg");
        ret.put("Journal", "journal");
        ret.put("Readable", "readable");
        ret.put("Simplex", "simplex");
        ret.put("Slate", "slate");
        ret.put("SpaceLab", "spacelab");
        ret.put("Spruce", "spruce");
        ret.put("SuperHero", "superhero");
        ret.put("United", "united");

        return ret;
    }

    public Resolution switchTheme(){
        getRequest().getSession().setAttribute(THEME_COOKIE, themeName);
        return new RedirectResolution("/themeRoller");
    }
}


