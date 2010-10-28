<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<w:facet object="${renderObjectResult.object}" facetName="renderTitle"/>
<%
    Map renderTitleResult = (Map)request.getAttribute("renderTitleResult");
    Object showTitle = renderTitleResult.get("showTitle");
    boolean includeTitle = showTitle instanceof Boolean && ((Boolean)showTitle);
%>
<div class="wokoObject">
    <c:if test="<%=includeTitle%>">
        <jsp:include page="${renderTitleResult.fragmentPath}"/>
    </c:if>
    <w:includeFacet object="${renderObjectResult.object}" facetName="renderProperties"/>
</div>