<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<c:set var="o" value="${home.facetContext.targetObject}"/>
<w:facet facetName="<%=WokoFacets.layout%>" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.guest.home.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <div class="pure-u-1">
            <h1 class="pure-u-1-2"><fmt:message bundle="${wokoBundle}" key="woko.guest.home.title"/></h1>
            <p class="pure-u-1-2">
                <fmt:message bundle="${wokoBundle}" key="woko.guest.home.content"/>
            </p>
        </div>

    </s:layout-component>
</s:layout-render>