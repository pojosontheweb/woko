package woko.mock;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockHttpSession;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.mock.MockServletContext;
import woko.Woko;
import woko.actions.WokoActionBean;
import woko.facets.FacetNotFoundException;
import woko.facets.ResolutionFacet;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for Mockroundtrip testing with Woko. Allows to implement unit tests that
 * access a fully-working Woko outside of a web container, in J2SE.
 *
 * Basically allows to recreate the MockServletContext and close it whenever needed, and
 * provides static helper functions.
 */
public class MockUtil {

    protected String contextName = "mockcontext";

    /**
     * Create and return the StripesFilter parameters map
     */
    protected Map<String,String> getParamsMap() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ActionResolver.Packages", "woko.actions");
        params.put("Extension.Packages", "woko.actions");
        return params;
    }

    /**
     * Create and return a ready-to-use MockServletContext
     * @param woko the configured Woko instance to be used
     */
    protected MockServletContext createMockServletContext(Woko woko) {
        MockServletContext mockServletContext = new CloseableMockServletContext(contextName);
        mockServletContext.addFilter(StripesFilter.class, "StripesFilter", getParamsMap());
        mockServletContext.setServlet(DispatcherServlet.class, "DispatcherServlet", null);
        mockServletContext.setAttribute(Woko.CTX_KEY, woko);
        return mockServletContext;
    }

    public static interface Callback {
        void execute(MockServletContext context) throws Exception;
    }

    public static interface CallbackWithResult<T> {
        T execute(MockServletContext context) throws Exception;
    }

    /**
     * Create servlet context, exec callback and close
     */
    public MockUtil withServletContext(Woko woko, Callback callback) throws Exception {
        MockServletContext mockServletContext = createMockServletContext(woko);
        try {
            callback.execute(mockServletContext);
        } finally {
            if (mockServletContext instanceof CloseableMockServletContext) {
                ((CloseableMockServletContext)mockServletContext).close();
            }
        }
        return this;
    }

    /**
     * Create servlet context, exec callback, close, and return the result of the callback
     */
    public <T> T withServletContextRetVal(Woko woko, CallbackWithResult<T> callback) throws Exception {
        MockServletContext mockServletContext = createMockServletContext(woko);
        try {
            return callback.execute(mockServletContext);
        } finally {
            if (mockServletContext instanceof CloseableMockServletContext) {
                ((CloseableMockServletContext)mockServletContext).close();
            }
        }
    }

    /**
     * Create, execute and return a <code>MockRoundtrip</code> for passed parameters
     * @param ctx the MockServletContext to be used
     * @param url the URL to invoke
     * @return the MockRoundtrip
     * @throws Exception
     */
    public static MockRoundtrip mockRoundtrip(MockServletContext ctx, String url) throws Exception {
        return mockRoundtrip(ctx, url, null);
    }

    /**
     * Create, execute and return a <code>MockRoundtrip</code> for passed parameters
     * @param ctx the MockServletContext to be used
     * @param url the URL to invoke
     * @param params additional parameters if any (null allowed)
     * @return the MockRoundtrip
     * @throws Exception
     */
    public static MockRoundtrip mockRoundtrip(MockServletContext ctx, String url, Map<String,String> params) throws Exception {
        return mockRoundtrip(ctx, url, params, null);
    }

    /**
     * Create, execute and return a <code>MockRoundtrip</code> for passed parameters
     * @param ctx the MockServletContext to be used
     * @param url the URL to invoke
     * @param params additional parameters if any (null allowed)
     * @param session the Http session if any (null allowed)
     * @return the MockRoundtrip
     * @throws Exception
     */
    public static MockRoundtrip mockRoundtrip(MockServletContext ctx, String url, Map<String,String> params, MockHttpSession session) throws Exception {
        MockRoundtrip t = session==null ? new MockRoundtrip(ctx, url) : new MockRoundtrip(ctx, url, session);
        if (params!=null) {
            for (String k : params.keySet()) {
                String v = params.get(k);
                t.addParameter(k, v);
            }
        }
        t.execute();
        return t;
    }

    /**
     * Create, execute and return a <code>MockRoundtrip</code> for passed parameters, by building a Woko URL
     * of type /{facetName}/{className}/{key}[?params].
     * @param ctx the MockServletContext to be used
     * @param facetName the facet name
     * @param className the class name
     * @param key the key
     * @return the MockRoundtrip
     * @throws Exception
     */
    public static MockRoundtrip mockRoundtrip(MockServletContext ctx, String facetName, String className, String key) throws Exception {
        return mockRoundtrip(ctx, facetName, className, key, null, null);
    }

    /**
     * Create, execute and return a <code>MockRoundtrip</code> for passed parameters, by building a Woko URL
     * of type /{facetName}/{className}/{key}[?params].
     * @param ctx the MockServletContext to be used
     * @param facetName the facet name
     * @param className the class name
     * @param key the key
     * @param params additional parameters if any (null allowed)
     * @return the MockRoundtrip
     * @throws Exception
     */
    public static MockRoundtrip mockRoundtrip(MockServletContext ctx, String facetName, String className, String key, Map<String,String> params) throws Exception {
        return mockRoundtrip(ctx, facetName, className, key, params, null);
    }

    /**
     * Create, execute and return a <code>MockRoundtrip</code> for passed parameters, by building a Woko URL
     * of type /{facetName}/{className}/{key}[?params].
     * @param ctx the MockServletContext to be used
     * @param facetName the facet name
     * @param className the class name
     * @param key the key
     * @param params additional parameters if any (null allowed)
     * @param session the Http session if any (null allowed)
     * @return the MockRoundtrip
     * @throws Exception
     */
    public static MockRoundtrip mockRoundtrip(MockServletContext ctx, String facetName, String className, String key, Map<String,String> params, MockHttpSession session) throws Exception {
        StringBuilder url = new StringBuilder("/").append(facetName);
        if (className!=null) {
            url.append("/").append(className);
        }
        if (key!=null) {
            url.append("/").append(key);
        }
        return mockRoundtrip(ctx, url.toString(), params, session);
    }

    // Various static helpers...
    // -------------------------

    public static <OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > WokoActionBean<OsType,UmType,UnsType,FdmType> tripAndGetWokoActionBean(MockServletContext mockServletContext,
                                                                             String url) throws Exception {
        Map<String,String> params = null;
        return tripAndGetWokoActionBean(mockServletContext, url, params, null);
    }

    public static <OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > WokoActionBean<OsType,UmType,UnsType,FdmType> tripAndGetWokoActionBean(MockServletContext mockServletContext,
                                                                             String url,
                                                                             Map<String,String> params) throws Exception {
        return tripAndGetWokoActionBean(mockServletContext, url, params, null);
    }

    public static <OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > WokoActionBean<OsType,UmType,UnsType,FdmType> tripAndGetWokoActionBean(MockServletContext mockServletContext,
                                                                             String url,
                                                                             Map<String,String> params,
                                                                             MockHttpSession session) throws Exception {
        MockRoundtrip trip = mockRoundtrip(mockServletContext, url, params, session);
        @SuppressWarnings("unchecked")
        WokoActionBean<OsType,UmType,UnsType,FdmType> wab = trip.getActionBean(WokoActionBean.class);
        return wab;
    }

    public static <OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > WokoActionBean<OsType,UmType,UnsType,FdmType> tripAndGetWokoActionBean(MockServletContext mockServletContext,
                                                                             String facetName,
                                                                             String className,
                                                                             String key) throws Exception {
        return tripAndGetWokoActionBean(mockServletContext, facetName, className, key, null, null);
    }

    public static <OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > WokoActionBean<OsType,UmType,UnsType,FdmType> tripAndGetWokoActionBean(MockServletContext mockServletContext,
                                                                             String facetName,
                                                                             String className,
                                                                             String key,
                                                                             Map<String,String> params) throws Exception {
        return tripAndGetWokoActionBean(mockServletContext, facetName, className, key, params, null);
    }

    public static <OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager
        > WokoActionBean<OsType,UmType,UnsType,FdmType> tripAndGetWokoActionBean(MockServletContext mockServletContext,
                                                                             String facetName,
                                                                             String className,
                                                                             String key,
                                                                             Map<String,String> params,
                                                                             MockHttpSession session) throws Exception {
        MockRoundtrip trip = mockRoundtrip(mockServletContext, facetName, className, key, params, session);
        @SuppressWarnings("unchecked")
        WokoActionBean<OsType,UmType,UnsType,FdmType> wab = trip.getActionBean(WokoActionBean.class);
        return wab;
    }

    public static ResolutionFacet tripAndGetFacet(MockServletContext mockServletContext, String url) throws Exception {
        Map<String,String> noParams = null; // ambigous method call if null passed
        return tripAndGetFacet(mockServletContext, url, noParams, null);
    }

    public static ResolutionFacet tripAndGetFacet(MockServletContext mockServletContext, String url, Map<String,String> params) throws Exception {
        return tripAndGetFacet(mockServletContext, url, params, null);
    }

    public static ResolutionFacet tripAndGetFacet(
            MockServletContext mockServletContext,
            String url,
            Map<String,String> params,
            MockHttpSession session) throws Exception {
        WokoActionBean wab = tripAndGetWokoActionBean(mockServletContext, url, params, session);
        return wab.getFacet();
    }

    public static ResolutionFacet tripAndGetFacet(
            MockServletContext mockServletContext,
            String facetName,
            String className,
            String key) throws Exception {
        return tripAndGetFacet(mockServletContext, facetName, className, key, null, null);
    }

    public static ResolutionFacet tripAndGetFacet(
            MockServletContext mockServletContext,
            String facetName,
            String className,
            String key,
            Map<String,String> params) throws Exception {
        return tripAndGetFacet(mockServletContext, facetName, className, key, params, null);
    }

    public static ResolutionFacet tripAndGetFacet(
            MockServletContext mockServletContext,
            String facetName,
            String className,
            String key,
            Map<String,String> params,
            MockHttpSession session) throws Exception {
        WokoActionBean wab = tripAndGetWokoActionBean(mockServletContext, facetName, className, key, params, session);
        return wab.getFacet();
    }

    public static boolean throwsFacetNotFound(
            MockServletContext ctx,
            String facetName,
            String className,
            String key,
            Map<String,String> params,
            MockHttpSession session) throws Exception {
        boolean hasThrown = false;
        try {
            tripAndGetFacet(ctx, facetName, className, key, params, session);
        } catch (Exception e) {
            if (e instanceof ServletException) {
                ServletException se = (ServletException)e;
                hasThrown = se.getCause() instanceof FacetNotFoundException;
            } else {
                throw e;
            }
        }
        return hasThrown;
    }

}

/**
 * Temporary remedy to :
 * http://www.stripesframework.org/jira/browse/STS-725
 * TODO remove
 */
class CloseableMockServletContext extends MockServletContext {

    CloseableMockServletContext(String contextName) {
        super(contextName);
    }

    public void close() {
        for (Filter f : getFilters()) {
            try {
                f.destroy();
            } catch (Exception e) {
                log("Exception while destroying filter " + f, e);
            }
        }
    }
}
