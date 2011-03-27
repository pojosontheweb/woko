<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul class="wokoObjectLinks">
    <c:forEach var="link" items="${renderLinks.links}">
        <li><a href="${pageContext.request.contextPath}/${link.href}" class="${link.cssClass}"><c:out value="${link.text}"/></a></li>
    </c:forEach>
</ul>