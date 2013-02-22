<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<li><a href="${pageContext.request.contextPath}/find"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.find"/> </a></li>
<li><a href="${pageContext.request.contextPath}/create"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.create"/> </a></li>

<li class="dropdown">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
        <fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.tools"/>
        <b class="caret"></b>
    </a>
    <ul class="dropdown-menu">
        <li><a href="${pageContext.request.contextPath}/studio"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.studio"/> </a></li>
        <w:includeFacet facetName="themeRollerNavBar"/>
    </ul>
</li>
