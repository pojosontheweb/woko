<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="<%=WokoFacets.layout%>" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>

<%-- Page title from application resources --%>
<fmt:message var="pageTitle" key="all.localization.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <%-- For title use a default woko message --%>
        <h1><fmt:message bundle="${wokoBundle}" key="woko.guest.home.title"/> </h1>
        <p>
            <%-- For content, override a woko message in application --%>
            <fmt:message key="woko.guest.home.content"/>
        </p>
    </s:layout-component>
</s:layout-render>