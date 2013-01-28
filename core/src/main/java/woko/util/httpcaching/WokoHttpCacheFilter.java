/*
 * Copyright 2001-2013 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package woko.util.httpcaching;

import woko.util.WLogger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request filter that helps browser caching by using a cacheToken
 * in the request parameters.
 * Automatically sets response headers for never-expiring resources.
 *
 * cacheToken request parameter and value are configurable.
 */
public class WokoHttpCacheFilter implements Filter {

    private static final long DELAY = 1000L * 3600L * 24L * 100L; // 100 days
    public static final String APP_CTX_ATTR_NAME = "wokoHttpCacheFilter";
    public static final String CACHE_TOKEN_DEFAULT_PARAM_NAME = "cacheToken";
    public static final String CFG_PARAM_NAME = "cacheTokenParamName";
    public static final String CFG_TOKEN_VALUE = "cacheTokenValue";
    public static final String HEADER_EXPIRES = "Expires";
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String CACHE_CONTROL_VALUE = "public, max-age=3153600";

    private static final WLogger logger = WLogger.getLogger(WokoHttpCacheFilter.class);

    private String cacheTokenValue = Long.toString(System.currentTimeMillis());
    private String cacheTokenParamName = CACHE_TOKEN_DEFAULT_PARAM_NAME;

    /**
     * Initialize the filter with values from config if any. Fallbacks to default
     * values if no init params provided in filter config.
     * Binds this to the servletContext so that the filter can be accessed
     * from the app.
     * @param filterConfig the filter config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String configParamName = filterConfig.getInitParameter(CFG_PARAM_NAME);
        if (configParamName!=null) {
            cacheTokenParamName = configParamName;
        }
        String configTokenValue = filterConfig.getInitParameter(CFG_TOKEN_VALUE);
        if (configTokenValue!=null) {
            cacheTokenValue = configTokenValue;
        }
        filterConfig.getServletContext().setAttribute(APP_CTX_ATTR_NAME, this);
        logger.info("Initialized :");
        logger.info("  * cacheTokenParamName = " + getCacheTokenParamName());
        logger.info("  * cacheTokenValue = " + getCacheTokenValue());
    }

    /**
     * Return the filter (if any) for passed servlet context.
     * @param ctx the servlet context
     * @return the filter instance
     */
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

            if (logger.isDebugEnabled()) {
                HttpServletRequest req = (HttpServletRequest)request;
                logger.debug("Applying headers for request URI=" + req.getRequestURI() + " queryString=" + req.getQueryString());
            }

            // cached resource, mark HTTP response headers as such
            long now = System.currentTimeMillis();
            HttpServletResponse resp = (HttpServletResponse)response;
            resp.setDateHeader(HEADER_EXPIRES, now + DELAY);
            resp.setDateHeader(HEADER_LAST_MODIFIED, now - DELAY);
            resp.setHeader(HEADER_CACHE_CONTROL, CACHE_CONTROL_VALUE);
        }

        chain.doFilter(request, response);
    }

    public String getCacheTokenValue() {
        return cacheTokenValue;
    }

    public String getCacheTokenParamName() {
        return cacheTokenParamName;
    }

    public void setCacheTokenValue(String cacheTokenValue) {
        this.cacheTokenValue = cacheTokenValue;
    }

    public void setCacheTokenParamName(String cacheTokenParamName) {
        this.cacheTokenParamName = cacheTokenParamName;
    }

    @Override
    public void destroy() {
    }
}
