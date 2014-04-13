<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.find.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <div class="container">
            <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.devel.find.pageTitle"/></h1>
            <h2><fmt:message bundle="${wokoBundle}" key="woko.devel.find.fullText"/> </h2>
            <s:form action="/search" class="form-inline" method="GET">
                <fmt:message bundle="${wokoBundle}" key="woko.devel.find.enterQuery"/>
                <s:text name="facet.query" class="input-xlarge"/>
                <s:submit name="search" class="btn btn-primary"/>
            </s:form>
            <h2><fmt:message bundle="${wokoBundle}" key="woko.devel.find.byClass"/> </h2>
            <p>
                <fmt:message bundle="${wokoBundle}" key="woko.devel.find.selectName"/>
            </p>
            <ul>
                <c:forEach items="${find.mappedClasses}" var="className">
                    <li><a href="${pageContext.request.contextPath}/list/${className}">${className}</a></li>
                </c:forEach>
            </ul>
        </div>
    </s:layout-component>
</s:layout-render>