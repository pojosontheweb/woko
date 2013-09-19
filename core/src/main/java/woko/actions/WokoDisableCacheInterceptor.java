package woko.actions;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import woko.facets.ResolutionFacet;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor that enforces no-caching policy for browsers, proxies etc. for
 * requests handled by a ResolutionFacet.
 *
 * No-cache is enforced by default. ResolutionFacets can bypass this interceptor by
 * implementing the markup interface {@link CustomCacheResolutionFacet}.
 *
 * Caching headers are
 * https://developers.google.com/speed/docs/best-practices/caching?hl=fr
 */
@Intercepts({LifecycleStage.BindingAndValidation})
public class WokoDisableCacheInterceptor implements Interceptor {

    private static final WLogger logger = WLogger.getLogger(WokoDisableCacheInterceptor.class);

    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CACHE_CONTROL_VALUE = "no-cache, no-store, must-revalidate";
    public static final String PRAGMA = "Pragma";
    public static final String PRAGMA_VALUE = "no-cache";
    public static final String EXPIRES = "Expires";

    public static void setNoCacheHeaders(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("marking response headers for URI = " + request.getRequestURI() + ", sessionId=" + request.getSession().getId());
        response.setHeader(CACHE_CONTROL, CACHE_CONTROL_VALUE); // HTTP 1.1.
        response.setHeader(PRAGMA, PRAGMA_VALUE); // HTTP 1.0.
        response.setDateHeader(EXPIRES, 0); // Proxies.
    }

    /**
     * Markup interface that allows <code>ResolutionFacet</code>s to bypass the http response
     * headers generation. <code>WokoDisableCacheInterceptor</code> won't set the caching headers for
     * resolution facets implementing this interface.
     */
    public static interface CustomCacheResolutionFacet {}

    @Override
    public Resolution intercept(ExecutionContext context) throws Exception {
        ActionBean ab = context.getActionBean();
        if (ab instanceof WokoActionBean) {
            ResolutionFacet facet = ((WokoActionBean<?,?,?,?>)ab).getFacet();
            if (facet!=null && !(facet instanceof CustomCacheResolutionFacet)) {
                ActionBeanContext abc = context.getActionBeanContext();
                HttpServletResponse response = abc.getResponse();
                if (response!=null) {
                    setNoCacheHeaders(abc.getRequest(), response);
                }
            }
        }
        return context.proceed();
    }



}
