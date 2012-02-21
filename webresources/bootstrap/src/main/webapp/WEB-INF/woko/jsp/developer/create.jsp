<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message var="pageTitle" key="woko.devel.create.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <h1 class="page-header"><fmt:message key="woko.devel.create.title"/></h1>
        <p>
            <fmt:message key="woko.devel.create.description"/>
        </p>
        <s:form action="/save" class="form-inline">
            <s:select name="className">
                <s:options-collection collection="${create.mappedClasses}"/>
            </s:select>
            <s:submit name="create" class="btn btn-primary"/>
        </s:form>
    </s:layout-component>
</s:layout-render>