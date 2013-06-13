package woko.facets.builtin.bootstrap.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.facets.builtin.all.LayoutAll;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <code>layout</code> facet for the bootstrap skin. Uses the default bootstrap for layouting
 * everything, and handles theming.
 */
public class LayoutBootstrap<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends LayoutAll<OsType,UmType,UnsType,FdmType> {

    private static final String JS_JQUERY = "/js/jQuery-V1.7.1/jquery.min.js";
    private static final String JS_BOOTSTRAP = "/js/bootstrap-v2.3.0/bootstrap.min.js";
    private static final String JS_BOOTSTRAP_DATEPICKER = "/js/bootstrap-datepicker/bootstrap-datepicker.min.js";
    private static final String JS_WOKO_BASE = "/woko/js/woko.base.js";
    private static final String JS_WOKO_JQUERY = "/woko/js/woko.jquery.js";
    private static final String JS_WOKO_RPC = "/woko/js/woko.rpc.js";

    private static final String CSS_BASE_BOOTSTRAP = "/css/bootstrap-v2.3.0/bootstrap.css";
    private static final String CSS_RESPONSIVE = "/css/responsive.css";
    private static final String CSS_WOKO = "/css/woko.css";


    @Override
    public List<String> getJsIncludes() {
        return Arrays.asList(JS_JQUERY, JS_BOOTSTRAP, JS_BOOTSTRAP_DATEPICKER, JS_WOKO_BASE, JS_WOKO_JQUERY, JS_WOKO_RPC);
    }

    /**
     * Return the CSS list for bootstrap or themed bootstrap if any.
     */
    @Override
    public List<String> getCssIncludes() {
        ArrayList<String> res = new ArrayList<String>();

        // handle theme
        HttpServletRequest request = getRequest();
        SwitchTheme<OsType,UmType,UnsType,FdmType> switchTheme = null;
        if (request!=null) {
            switchTheme = getWoko().getFacet(SwitchTheme.FACET_NAME, request, getFacetContext().getTargetObject());
        }
        if (switchTheme==null) {
            res.add(CSS_BASE_BOOTSTRAP);
        } else {
            Theme userTheme = switchTheme.getUserTheme();
            if (userTheme==null) {
                res.add(CSS_BASE_BOOTSTRAP);
            } else {
                // user theme available, use this one !
                res.add("/css/" + userTheme.name().toLowerCase() + "/bootstrap.css");
            }
        }

        res.add(CSS_RESPONSIVE);
        res.add(CSS_WOKO);

        return res;
    }

}
