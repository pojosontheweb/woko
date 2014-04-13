<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.facets.builtin.RenderListItem" %>
<%@ page import="woko.persistence.ObjectStore" %>


<%
    RenderListItem listItem = (RenderListItem)request.getAttribute(WokoFacets.renderListItem);
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)listItem.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();

    Object result = fctx.getTargetObject();
    String title = Util.getTitle(request, result);
    // compute link if view facet is available
    String href = null;
    String resultKey = woko.getObjectStore().getKey(result);
    ObjectStore s = woko.getObjectStore();
    String resultClassName = s.getClassMapping(s.getObjectClass(result));
    if (woko.getFacet(WokoFacets.view, request, result)!=null) {
        href = new StringBuilder().
                append(request.getContextPath()).
                append("/").
                append(WokoFacets.view).
                append("/").
                append(resultClassName).
                append("/").
                append(resultKey).
                toString();
    }
%>
<%=resultKey%> -
<%
    if (href!=null) {
%>
<a href="<%=href%>">
    <%
        }
    %>
    <c:out value="<%=title%>"/>
    <%
        if (href!=null) {
    %>
</a>
<%
    }
%>
(<%=resultClassName%>)
