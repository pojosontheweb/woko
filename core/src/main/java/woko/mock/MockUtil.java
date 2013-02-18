package woko.mock;

import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.mock.MockServletContext;
import woko.Woko;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for Mockroundtrip testing with Woko. Allows to implement unit tests that
 * access a fully-working Woko outside of a web container, in J2SE.
 *
 * Basically allows to recreate the MockServletContext and close it whenever needed, and
 * provide helper functions to create <code>MockRoundtrip</code>s.
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
        MockServletContext mockServletContext = new MockServletContext(contextName);
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
            mockServletContext.close();
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
            mockServletContext.close();
        }
    }

    /**
     * Create, execute and return a <code>MockRoundtrip</code> for passed parameters
     * @param ctx the MockServletContext to be used
     * @param url the URL to invoke
     * @param params additonal parameters if any
     * @return the MockRoundtrip
     * @throws Exception
     */
    public static MockRoundtrip mockRoundtrip(MockServletContext ctx, String url, Map<String,String> params) throws Exception {
        MockRoundtrip t = new MockRoundtrip(ctx, url);
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
     * @param params the optional params (null or empty map allowed)
     * @return the MockRoundtrip
     * @throws Exception
     */
    public static MockRoundtrip mockRoundtrip(MockServletContext ctx, String facetName, String className, String key, Map<String,String> params) throws Exception {
        StringBuilder url = new StringBuilder("/").append(facetName);
        if (className!=null) {
            url.append("/").append(className);
        }
        if (key!=null) {
            url.append("/").append(key);
        }
        return mockRoundtrip(ctx, url.toString(), params);
    }

}
