<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<c:set var="categ" value="${fragment.facetContext.targetObject}"/>
<li>
    <w:link object="${categ}" facetName="view"/>
    <c:set var="subCategs" value="${categ.subCategories}"/>
    <c:if test="${not empty subCategs}">
        <ul>
            <c:forEach var="sub" items="${subCategs}">
                <w:includeFacet targetObject="${sub}" facetName="fragment"/>
            </c:forEach>
        </ul>
    </c:if>
</li>
