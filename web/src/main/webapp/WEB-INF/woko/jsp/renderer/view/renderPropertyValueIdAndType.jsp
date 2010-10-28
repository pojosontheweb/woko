<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="v" value="${renderPropertyValueResult.propertyValue}"/>
<span class="wokoPropertyValue">
    <span class="${v.class.simpleName}">
        <strong><c:out value="${v}"/></strong>        
    </span>
</span>