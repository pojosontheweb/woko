<%@ page import="java.util.Map" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.Woko" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<w:facet object="${renderObjectEditResult.object}" facetName="renderTitle"/>
<%
    Woko woko = Woko.getWoko(application);
    Map renderTitleResult = (Map)request.getAttribute("renderTitleResult");
    Object showTitle = renderTitleResult.get("showTitle");
    boolean includeTitle = showTitle instanceof Boolean && ((Boolean)showTitle);
    Map roer = (Map)request.getAttribute("renderObjectEditResult");
    Object object = roer.get("object");
%>
<div class="wokoObject">
    <c:if test="<%=includeTitle%>">
        <jsp:include page="${renderTitleResult.fragmentPath}"/>
    </c:if>
    <s:form action="<%=Util.facetUrl(woko, "save", object)%>">
        <w:includeFacet object="${renderObjectEditResult.object}" facetName="renderPropertiesEdit"/>
        <s:submit name="save"/>
    </s:form>
</div>