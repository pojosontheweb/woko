<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>

<c:set var="o" value="${delete.facetContext.targetObject}"/>
<w:facet facetName="<%=WokoFacets.layout%>" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${renderTitle.title}">
    <s:layout-component name="body">
        <div class="container">
            <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.devel.confirmDelete.title"/></h1>
            <p>
                <fmt:message bundle="${wokoBundle}" key="woko.devel.confirmDelete.question">
                    <fmt:param><w:title object="${o}"/></fmt:param>
                </fmt:message>

                <w:objectClassName var="className" object="${o}"/>
                <w:objectKey var="key" object="${o}"/>
            </p>
            <s:form action="${pageContext.request.contextPath}/delete/${className}/${key}" class="form-inline">
                <s:submit name="facet.confirm" class="btn btn-primary" value="true"/>
                <s:submit name="facet.cancel" class="btn" value="true"/>
            </s:form>
        </div>

    </s:layout-component>
</s:layout-render>
