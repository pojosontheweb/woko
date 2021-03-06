<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.builtin.*" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.password.reset.confirm.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.reset.confirm.h1.text"/>
        </h1>

        <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.reset.confirm.para.text">
            <fmt:param value="${resetPassword.email}"/>
        </fmt:message>

    </s:layout-component>
</s:layout-render>

