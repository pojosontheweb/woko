<%@ include file="../../woko/jsp/taglibs.jsp"%>
<%@ tag import="woko.Woko" %>
<%@ tag import="woko.facets.builtin.RenderTitle" %>
<%@ attribute name="object" required="true" type="java.lang.Object" %>
<%
    Woko woko = Woko.getWoko(application);
    RenderTitle renderTitle = (RenderTitle)woko.getFacet(RenderTitle.FACET_NAME, request, object);
    String title = null;
    if (renderTitle!=null) {
        title = renderTitle.getTitle();
    }
%><c:out value="<%=title%>"/>