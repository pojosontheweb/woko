<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.builtin.*" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>
<c:set var="regDetails" value="${activate.facetContext.targetObject}"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.activate.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.activate.h1.text"/>
        </h1>

        <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.activate.para.text">
            <fmt:param value="${regDetails.user.username}"/>
        </fmt:message>

    </s:layout-component>
</s:layout-render>

