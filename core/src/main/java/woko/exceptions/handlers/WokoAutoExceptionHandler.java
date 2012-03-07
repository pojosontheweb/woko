package woko.exceptions.handlers;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.AutoExceptionHandler;
import woko.facets.FacetNotFoundException;
import woko.util.WLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public Resolution handleFacetNotFoundException(FacetNotFoundException exc, HttpServletRequest request, HttpServletResponse response) {
        String ticket = genTicket(request);
        logger.warn("FacetNotFoundException caught by the WokoAutoExceptionHandler - ticket : " + ticket, exc);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setHeader(REQ_ATTR_TICKET, ticket);
        return new ForwardResolution("/WEB-INF/woko/jsp/exception-404.jsp");
    }

    public Resolution handleGenericException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        String ticket = genTicket(request);
        logger.error("Exception caught by the WokoAutoExceptionHandler - ticket : " + ticket, e);
        response.setHeader(REQ_ATTR_TICKET, ticket);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ForwardResolution("/WEB-INF/woko/jsp/exception-500.jsp");
    }


}
