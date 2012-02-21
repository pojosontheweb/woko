<%@ tag import="woko.facets.builtin.WokoFacets" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ attribute name="object" required="true" type="java.lang.Object" %>
<%@ attribute name="facetName" required="false" type="java.lang.String" %>
<c:if test="facetName==null">
    <c:set var="facetName" value="<%=WokoFacets.view%>"/>
</c:if>
<w:url object="${object}" var="objectUrl" facetName="${facetName}"/>
<w:facet facetName="<%=WokoFacets.renderTitle%>" targetObject="${object}"/>
<a href="${objectUrl}"><c:out value="${renderTitle.title}"/></a>