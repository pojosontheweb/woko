<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.RenderTitle" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Woko woko = Woko.getWoko(application);
    RenderTitle renderTitle = (RenderTitle)request.getAttribute(WokoFacets.renderTitle);
    Object target = renderTitle.getFacetContext().getTargetObject();
    Class<?> targetClass = target.getClass();
    String className = woko.getObjectStore().getClassMapping(targetClass);
%>
<h1 class="page-header">${renderTitle.title} <small>(<%=className%>)</small></h1>