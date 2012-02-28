<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ul>
    <li><a href="${pageContext.request.contextPath}/home"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.home"/> </a></li>
    <li><a href="${pageContext.request.contextPath}/find"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.find"/> </a></li>
    <li><a href="${pageContext.request.contextPath}/create"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.create"/> </a></li>
    <li><a href="${pageContext.request.contextPath}/studio"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.studio"/> </a></li>
    <li><a href="${pageContext.request.contextPath}/users">users</a></li>
</ul>