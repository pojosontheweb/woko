<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<li class="dropdown">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
        Theme roller
        <b class="caret"></b>
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
