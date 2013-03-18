<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<h1 class="page-header">
    <c:forEach items="${renderTitle.fullPath}" var="categ" varStatus="status">
        <c:choose>
            <c:when test="${status.last}">
                <c:out value="${categ.name}"/>
            </c:when>
            <c:otherwise>
                <w:url object="${categ}" var="categUrl"/>
                <a href="${categUrl}">
                    <c:out value="${categ.name}"/>
                </a>
                ${renderTitle.pathSeparator}
            </c:otherwise>
        </c:choose>
    </c:forEach>
</h1>