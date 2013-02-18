package woko.mock;

import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.mock.MockServletContext;
import woko.Woko;

import java.util.HashMap;
import java.util.Map;

public class MockUtil {

    protected MockServletContext createMockServletContext(Woko woko) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("ActionResolver.Packages", "woko.actions");
        params.put("Extension.Packages", "woko.actions");
        MockServletContext mockServletContext = new MockServletContext("mockctx");
        mockServletContext.addFilter(StripesFilter.class, "StripesFilter", params);
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

    public MockUtil withServletContext(Woko woko, Callback callback) throws Exception {
        MockServletContext mockServletContext = createMockServletContext(woko);
        try {
            callback.execute(mockServletContext);
        } finally {
            mockServletContext.close();
        }
        return this;
    }

    public <T> T withServletContextRetVal(Woko woko, CallbackWithResult<T> callback) throws Exception {
        MockServletContext mockServletContext = createMockServletContext(woko);
        try {
            return callback.execute(mockServletContext);
        } finally {
            mockServletContext.close();
        }
    }

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
