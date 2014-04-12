<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.facets.builtin.RenderListItem" %>
<%@ page import="test.MyEntity" %>
<%@ page import="test.MyBook" %>


<%
    RenderListItem listItem = (RenderListItem)request.getAttribute(WokoFacets.renderListItem);
    WokoFacetContext fctx = (WokoFacetContext)listItem.getFacetContext();

    MyBook result = (MyBook)fctx.getTargetObject();

%>
Name : <c:out value="<%=result.getName()%>"/> - NbPages : <c:out value="<%=result.getNbPages()%>"/>
