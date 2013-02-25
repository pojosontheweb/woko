<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<li class="dropdown-submenu">
    <a tabindex="-1" href="#">
        <fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.themeRoller"/>
    </a>
    <ul class="dropdown-menu">
        <c:forEach items="${themeRollerNavBar.availableThemes}" var="theme">
            <li>
                <a href="${pageContext.request.contextPath}/theme/${theme.value}">
                    ${theme.key}
                </a>
            </li>
        </c:forEach>
    </ul>
</li>
