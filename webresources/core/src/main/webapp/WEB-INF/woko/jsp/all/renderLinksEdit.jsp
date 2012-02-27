<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<ul class="wokoObjectLinks">
    <c:forEach var="link" items="${renderLinksEdit.links}">
      <li><a href="${pageContext.request.contextPath}/${link.href}" class="${link.cssClass}"><c:out value="${link.text}"/></a></li>
    </c:forEach>
</ul>