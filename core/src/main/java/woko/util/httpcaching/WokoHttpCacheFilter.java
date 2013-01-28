package woko.util.httpcaching;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WokoHttpCacheFilter implements Filter {

    private static final long DELAY = 1000L * 3600L * 24L * 100L; // 100 days
    public static final String APP_CTX_ATTR_NAME = "wokoHttpCacheFilter";

    private String cacheTokenValue;
    private String cacheTokenParamName = "cacheToken";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        cacheTokenValue = Long.toString(System.currentTimeMillis());
        filterConfig.getServletContext().setAttribute(APP_CTX_ATTR_NAME, this);
    }

    public static WokoHttpCacheFilter get(ServletContext ctx) {
        return (WokoHttpCacheFilter)ctx.getAttribute(APP_CTX_ATTR_NAME);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // check that cache token is provided in request, and that it is equals to the token we
        // have for the app, so that we cache only what really meant to be cached and
        // disable caching for "random" values of the cache token.
        String requestCacheToken = request.getParameter(getCacheTokenParamName());
        if (requestCacheToken!=null && requestCacheToken.equals(getCacheTokenValue())) {

            // cached resource, mark HTTP response headers as such
            long now = System.currentTimeMillis();
            HttpServletResponse resp = (HttpServletResponse)response;
            resp.setDateHeader("Expires", now + DELAY);
            resp.setDateHeader("Last-Modified", now - DELAY);
            resp.setHeader("Cache-Control", "public, max-age=3153600");
        }

        chain.doFilter(request, response);
    }

    public String getCacheTokenValue() {
        return cacheTokenValue;
    }

    public String getCacheTokenParamName() {
        return cacheTokenParamName;
    }

    @Override
    public void destroy() {

    }
}
