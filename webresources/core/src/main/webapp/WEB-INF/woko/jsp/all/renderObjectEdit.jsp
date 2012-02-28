<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<c:set var="o" value="${renderObjectEdit.facetContext.targetObject}"/>
<div class="wokoObject">
    <w:includeFacet targetObject="${o}" facetName="<%=WokoFacets.renderLinksEdit%>"/>
    <w:includeFacet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>
    <w:includeFacet targetObject="${o}" facetName="<%=WokoFacets.renderPropertiesEdit%>"/>
</div>