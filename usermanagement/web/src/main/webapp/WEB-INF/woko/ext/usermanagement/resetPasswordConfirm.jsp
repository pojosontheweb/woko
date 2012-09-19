<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.builtin.*" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.reset.password.confirm.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.reset.password.confirm.h1.text"/>
        </h1>

        <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.reset.password.confirm.para.text"/>

        <p><b>${resetPasswordConfirm.newPassword}</b></p>

    </s:layout-component>
</s:layout-render>

