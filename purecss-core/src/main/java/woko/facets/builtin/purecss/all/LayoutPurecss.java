package woko.facets.builtin.purecss.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.facets.builtin.all.LayoutAll;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.lang.Override;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>layout</code> facet for the bootstrap skin. Uses the default bootstrap for layouting
 * everything, and handles theming.
 */
public class LayoutPurecss<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends LayoutAll<OsType,UmType,UnsType,FdmType> {

    private static final String CSS_BASE_PURE = "/css/pure-0.2.0.css";
    private static final String CSS_WOKO = "/css/woko.css";
    private static final String JS_YUI = "/js/yui-3.10.3.js";
    private static final String OMNES_PRO_PROXIMA_NOVA_CSS = "/css/omnes-pro_proxima-nova.css";
    /**
     * Return the CSS list for bootstrap or themed bootstrap if any.
     */
    @Override
    public List<String> getCssIncludes() {
        ArrayList<String> res = new ArrayList<String>();

        res.add(OMNES_PRO_PROXIMA_NOVA_CSS);
        res.add(CSS_BASE_PURE);
        res.add(CSS_WOKO);
        return res;
    }

    @Override
    public List<String> getJsIncludes() {
        ArrayList<String> res = new ArrayList<String>(super.getJsIncludes());
        res.add(JS_YUI);

        return res;
    }
}
