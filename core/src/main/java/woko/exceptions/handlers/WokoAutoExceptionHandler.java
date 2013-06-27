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
import org.json.JSONObject;
import woko.facets.FacetNotFoundException;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Stripes Exception Handler that manages RPC calls and error ticket.
 * For RPC calls, Exceptions will be serialized to JSON.
 * An error ticket (UUID) is generated when an exception is caught, and added to the response,
 * so that it can be displayed in the UI and used to grep the logs.
 */
public class WokoAutoExceptionHandler implements AutoExceptionHandler {

    private static final String REQ_ATTR_TICKET = "wokoErrorTicket";
    private static final WLogger logger = WLogger.getLogger(WokoAutoExceptionHandler.class);

    /**
     * Generates a new ticket and binds it to the request
     * @param request the request
     * @return a freshly generated ticket
     */
    private String genTicket(HttpServletRequest request) {
        String ticket = UUID.randomUUID().toString();
        request.setAttribute(REQ_ATTR_TICKET, ticket);
        return ticket;
    }

    /**
     * Return the ticket bound to passed request (if any)
     * @param request the request
     * @return the ticket found in request if any, null otherwise
     */
    public static String getTicket(HttpServletRequest request) {
        return (String)request.getAttribute(REQ_ATTR_TICKET);
    }

    /**
     * Helper method that creates a <code>Resolution</code> with JSON data used
     * to handle the error client side, for RPC calls
     * @param message the error message
     * @param ticket the ticket
     * @return a JSON representation (streamed) of the error
     */
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

    /**
     * Invoked on {@link FacetNotFoundException} : set response 404 header, and forwards to the 404 error JSP (or returns JSON for RPC)
     * @param exc the exception
     * @param request the request
     * @param response the response
     * @return a <code>Resolution</code> for the exception
     */
    public Resolution handleFacetNotFoundException(FacetNotFoundException exc, HttpServletRequest request, HttpServletResponse response) {
        return handle(exc, request, response, "/WEB-INF/woko/jsp/exception-404.jsp", HttpServletResponse.SC_NOT_FOUND, "requested resource not found", false);
    }

    /**
     * Invoked on generic exceptions : set response 500 header, forwards to the 500 error JSP (or returns JSON for RPC)
     * @param e the exception
     * @param request the request
     * @param response the response
     * @return a <code>Resolution</code> for the exception
     */
    public Resolution handleGenericException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        return handle(e, request, response, "/WEB-INF/woko/jsp/exception-500.jsp", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), true);
    }

    protected Resolution handle(Exception e,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                String pathToJsp,
                                int errorCode,
                                String rpcMessage,
                                boolean showFullStack) {
        String ticket = genTicket(request);
        if (showFullStack) {
            logger.error("Exception caught by the WokoAutoExceptionHandler - ticket : " + ticket, e);
        } else {
            logger.error("Exception caught by the WokoAutoExceptionHandler - ticket : " + ticket +
                        ", message=" + e.getMessage());
        }
        response.setHeader(REQ_ATTR_TICKET, ticket);
        response.setStatus(errorCode);
        if (RpcInterceptor.isRpcRequest(request)) {
            return createResolutionForRpc(rpcMessage, ticket);
        }
        return new ForwardResolution(pathToJsp);
    }


}
