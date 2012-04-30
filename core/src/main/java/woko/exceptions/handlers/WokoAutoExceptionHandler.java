/*
 * Copyright 2001-2012 Remi Vankeisbelck
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

package woko.exceptions.handlers;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.exception.AutoExceptionHandler;
import net.sourceforge.stripes.rpc.RpcInterceptor;
import org.json.JSONException;
import org.json.JSONObject;
import woko.facets.FacetNotFoundException;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

public class WokoAutoExceptionHandler implements AutoExceptionHandler {

    private static final String REQ_ATTR_TICKET = "wokoErrorTicket";
    private static final WLogger logger = WLogger.getLogger(WokoAutoExceptionHandler.class);

    private String genTicket(HttpServletRequest request) {
        String ticket = UUID.randomUUID().toString();
        request.setAttribute(REQ_ATTR_TICKET, ticket);
        return ticket;
    }

    public static String getTicket(HttpServletRequest request) {
        return (String)request.getAttribute(REQ_ATTR_TICKET);
    }

    public Resolution createResolutionForRpc(String message, String ticket) {
        JSONObject j = new JSONObject();
        try {
            j.put("error", true);
            j.put("message", message);
            j.put("ticket", ticket);
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
        return new StreamingResolution("text/json", j.toString());
    }

    public Resolution handleFacetNotFoundException(FacetNotFoundException exc, HttpServletRequest request, HttpServletResponse response) {
        String ticket = genTicket(request);
        logger.warn("FacetNotFoundException caught by the WokoAutoExceptionHandler - ticket : " + ticket, exc);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setHeader(REQ_ATTR_TICKET, ticket);
        if (RpcInterceptor.isRpcRequest(request)) {
            return createResolutionForRpc("requested resource not found", ticket);
        }
        return new ForwardResolution("/WEB-INF/woko/jsp/exception-404.jsp");
    }

    public Resolution handleGenericException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        String ticket = genTicket(request);
        logger.error("Exception caught by the WokoAutoExceptionHandler - ticket : " + ticket, e);
        response.setHeader(REQ_ATTR_TICKET, ticket);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        if (RpcInterceptor.isRpcRequest(request)) {
            return createResolutionForRpc(e.getMessage(), ticket);
        }
        return new ForwardResolution("/WEB-INF/woko/jsp/exception-500.jsp");
    }


}
