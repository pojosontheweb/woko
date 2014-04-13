<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.create.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <div class="container">
            <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.devel.create.title"/></h1>
            <p>
                <fmt:message bundle="${wokoBundle}" key="woko.devel.create.description"/>
            </p>
            <s:form action="/save" class="form-inline" method="GET">
                <s:hidden name="createTransient" value="true"/>
                <s:select name="className" class="form-control">
                    <s:options-collection collection="${create.mappedClasses}"/>
                </s:select>
                <s:submit name="create" class="btn btn-primary"/>
            </s:form>
        </div>
    </s:layout-component>
</s:layout-render>