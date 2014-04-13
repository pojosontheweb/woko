<%@ tag import="net.sourceforge.stripes.action.Message" %>
<%@ tag import="java.util.List" %>
<%@ tag import="net.sourceforge.stripes.controller.StripesConstants" %>
<%@ tag import="woko.Woko" %>
<%@ tag import="woko.facets.builtin.RenderMessages" %>
<%@ attribute name="key" required="false" type="java.lang.String" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    if (key==null) {
        key = StripesConstants.REQ_ATTR_MESSAGES;
    }
    List<Message> messages = (List<Message>)request.getAttribute( key );

    if (messages == null) {
        if (session != null) {
            messages = (List<Message>) session.getAttribute( key );
            session.removeAttribute( key );
        }
    }

    if (messages!=null) {
        // check if we have a renderMessages facet, and if so, use it
        Woko<?,?,?,?> woko = Woko.getWoko(application);
        RenderMessages renderMessages = woko.getFacet(RenderMessages.FACET_NAME, request, messages);
        if (renderMessages!=null) {
            // use the facet to render the messages
            String fragmentPath = renderMessages.getFragmentPath(request);
%>
            <jsp:include page="<%=fragmentPath%>"/>
<%
        } else {
            // default to stripes messages handling
%>
            <s:messages key="<%=key%>"/>
<%
        }
    }
%>