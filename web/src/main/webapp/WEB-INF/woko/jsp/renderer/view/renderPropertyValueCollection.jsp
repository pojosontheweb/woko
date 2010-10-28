<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<c:set var="v" value="${renderPropertyValueResult.propertyValue}"/>
<span class="wokoPropertyValue">
    <span class="Collection">
        <c:forEach items="${renderPropertyValueResult.propertyValue}" var="elem">
            <div class="wokoCollectionElement"><w:includeFacet facetName="renderPropertyValue" object="${elem}"/></div>
        </c:forEach>
    </span>
</span>