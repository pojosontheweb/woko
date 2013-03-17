<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.categories.manage.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <div class="row-fluid">
            <h1 class="page-header">
                <fmt:message bundle="${wokoBundle}" key="woko.ext.categories.manage.title"/>
            </h1>
        </div>

        <c:set var="rootCategories" value="${manageCategories.rootCategories}"/>
        <c:choose>
            <c:when test="${not empty rootCategories}">
                <ul>
                    <c:forEach items="${rootCategories}" var="categ">
                        <li>
                            <w:link object="${categ}" facetName="view"/>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>

                <p>
                    <fmt:message bundle="${wokoBundle}" key="woko.ext.categories.manage.no.categs.yet"/>
                </p>

            </c:otherwise>
        </c:choose>
    </s:layout-component>
</s:layout-render>