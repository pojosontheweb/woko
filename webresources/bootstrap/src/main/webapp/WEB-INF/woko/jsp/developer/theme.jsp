<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<c:forEach items="${theme.availableThemes}" var="theme">
    <li>
        <a href="${pageContext.request.contextPath}/theme?facet.themeName=${theme.value}&switchTheme=true">
                ${theme.key}
        </a>
    </li>
</c:forEach>