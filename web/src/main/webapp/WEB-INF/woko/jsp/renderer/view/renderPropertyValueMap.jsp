<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<c:set var="prevRpr" value="${renderPropertiesResult}" scope="page"/>
<span class="wokoPropertyValue">
    <span class="Map">
        <w:includeFacet object="${renderPropertyValueResult.propertyValue}" facetName="renderProperties"/>
    </span>
</span>
<c:set var="renderPropertiesResult" scope="request" value="${prevRpr}"/> 