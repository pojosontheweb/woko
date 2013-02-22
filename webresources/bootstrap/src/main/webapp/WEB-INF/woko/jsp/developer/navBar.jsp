<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<li><a href="${pageContext.request.contextPath}/find"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.find"/> </a></li>
<li><a href="${pageContext.request.contextPath}/create"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.create"/> </a></li>
<li><a href="${pageContext.request.contextPath}/studio"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.studio"/> </a></li>

<%-- Specific dropdown menu for themeRoller --%>
<w:includeFacet facetName="themeRollerNavBar"/>