<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.create.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <h1><fmt:message bundle="${wokoBundle}" key="woko.devel.create.title"/></h1>
        <p>
            <fmt:message bundle="${wokoBundle}" key="woko.devel.create.description"/>
        </p>
        <s:form action="/save">
            <s:select name="className">
                <s:options-collection collection="${create.mappedClasses}"/>
            </s:select>
            <s:submit name="create"/>
        </s:form>
    </s:layout-component>
</s:layout-render>