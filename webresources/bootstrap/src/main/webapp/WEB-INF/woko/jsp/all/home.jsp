<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="<%=WokoFacets.layout%>" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.guest.home.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <div class="row-fluid">
            <div class="span12">
                <div class="hero-unit">
                    <h1><fmt:message bundle="${wokoBundle}" key="woko.guest.home.title"/></h1>
                    <p>
                        <fmt:message bundle="${wokoBundle}" key="woko.guest.home.content"/>
                    </p>
                </div>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>