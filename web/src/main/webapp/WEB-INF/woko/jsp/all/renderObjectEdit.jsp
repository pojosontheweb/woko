<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="o" value="${renderObjectEdit.facetContext.targetObject}"/>
<div class="wokoObject">
    <w:includeFacet targetObject="${o}" facetName="renderLinksEdit"/>
    <w:includeFacet targetObject="${o}" facetName="renderTitle"/>
    <w:includeFacet targetObject="${o}" facetName="renderPropertiesEdit"/>
</div>