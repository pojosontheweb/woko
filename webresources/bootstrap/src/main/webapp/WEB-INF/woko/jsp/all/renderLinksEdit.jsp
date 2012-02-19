<%@ page import="woko.facets.builtin.RenderLinks" %>
<%@ page import="woko.facets.builtin.all.Link" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    RenderLinks renderLinks = (RenderLinks)request.getAttribute("renderLinksEdit");
    List<Link> links = renderLinks.getLinks();
    if (links.size()>=1) {
        Link first = links.get(0);
%>
<div class="btn-group">
    <a class="btn" href="<%=request.getContextPath() + "/" + first.getHref()%>"
      class="link-<%=first.getCssClass()%>"><%=first.getText()%></a>
    <%
        if (links.size()>1) {
    %>
        <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
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