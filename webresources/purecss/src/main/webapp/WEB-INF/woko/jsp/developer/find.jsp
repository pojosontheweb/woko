<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.find.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <h1 class="pure-menu-heading"><fmt:message bundle="${wokoBundle}" key="woko.devel.find.pageTitle"/></h1>
        <h2><fmt:message bundle="${wokoBundle}" key="woko.devel.find.fullText"/> </h2>
        <s:form action="/search" class="pure-form" method="GET">
            <fmt:message bundle="${wokoBundle}" key="woko.devel.find.enterQuery"/>
            <s:text name="facet.query" class="pure-input-1-3"/>
            <s:submit name="search" class="pure-button pure-button-primary"/>
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
    </s:layout-component>
</s:layout-render>