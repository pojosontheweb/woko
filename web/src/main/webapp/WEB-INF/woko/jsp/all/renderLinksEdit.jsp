<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="wokoLinksEdit">
    <ul>
        <c:forEach var="link" items="${renderLinksEdit.links}">
            <li><a href="${pageContext.request.contextPath}/${link.href}"><c:out value="${link.text}"/></a></li>            
        </c:forEach>
    </ul>
</div>