<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.blobs.upload.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.ext.blobs.upload.title"/></h1>
        <s:form action="/upload" class="form-inline">
            <s:hidden name="className"/>
            <s:file name="facet.data"/>
            <s:submit name="upload" class="btn btn-primary"/>
        </s:form>
    </s:layout-component>
</s:layout-render>