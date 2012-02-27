<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderLinks" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.all.Link" %>
<%@ page import="java.util.List" %>

<%
    RenderLinks renderLinks = (RenderLinks)request.getAttribute(WokoFacets.renderLinks);
    List<Link> links = renderLinks.getLinks();
    if (links.size()>=1) {
        Link first = links.get(0);
%>
<div class="btn-group">
    <a class="btn btn-primary" href="<%=request.getContextPath() + "/" + first.getHref()%>"
      class="link-<%=first.getCssClass()%>"><%=first.getText()%></a>
    <%
        if (links.size()>1) {
    %>
        <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#">
            <span class="caret"></span>
        </a>
        <ul class="dropdown-menu">
    <%
            for (int i=1; i<links.size(); i++) {
                Link l = links.get(i);
    %>
            <li><a href="<%=request.getContextPath() + "/" + l.getHref()%>" class="link-<%=l.getCssClass()%>"><%=l.getText()%></a> </li>
    <%      } %>
        </ul>
    <% } %>
</div>
<%
    }
%>