<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="o" value="${renderObject.facetContext.targetObject}"/>
<div class="wokoObject">
    <w:includeFacet targetObject="${o}" facetName="<%=WokoFacets.renderLinks%>"/>
    <w:includeFacet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>
    <w:includeFacet targetObject="${o}" facetName="<%=WokoFacets.renderProperties%>"/>
</div>