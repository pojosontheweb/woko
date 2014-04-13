package woko.facets.builtin.bootstrap3.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import woko.facets.builtin.all.LayoutAll;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <code>layout</code> facet for the bootstrap3 skin.
 */
public class LayoutBootstrap3<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends LayoutAll<OsType,UmType,UnsType,FdmType> {

    private static final String JS_JQUERY = "/js/jQuery-V1.9.1/jquery.min.js";
    private static final String JS_BOOTSTRAP = "/bootstrap-3.1.1-dist/js/bootstrap.min.js";
    private static final String JS_BOOTSTRAP_DATEPICKER = "/js/bootstrap3-datepicker/bootstrap-datepicker.min.js";
    private static final String JS_WOKO_BASE = "/woko/js/woko.base.js";
    private static final String JS_WOKO_JQUERY = "/woko/js/woko.jquery.js";
    private static final String JS_WOKO_RPC = "/woko/js/woko.rpc.js";

    private static final String CSS_BOOTSTRAP = "/bootstrap-3.1.1-dist/css/bootstrap.min.css";
    private static final String CSS_WOKO = "/css/woko.css";

    private static final List<String> CSS_INCLUDES = Collections.unmodifiableList(
            Arrays.asList(CSS_BOOTSTRAP, CSS_WOKO)
    );

    private static final List<String> JS_INCLUDES = Collections.unmodifiableList(
            Arrays.asList(JS_JQUERY, JS_BOOTSTRAP, JS_BOOTSTRAP_DATEPICKER, JS_WOKO_BASE, JS_WOKO_JQUERY, JS_WOKO_RPC)
    );

    @Override
    public List<String> getJsIncludes() {
        return JS_INCLUDES;
    }

    @Override
    public List<String> getCssIncludes() {
        return CSS_INCLUDES;
    }

}
