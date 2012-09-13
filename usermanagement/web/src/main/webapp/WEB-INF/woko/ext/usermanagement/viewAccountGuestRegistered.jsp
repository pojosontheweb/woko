<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.builtin.*" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.guest.account.registered.page.title"/>
<c:set var="user" value="${view.facetContext.targetObject}"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.guest.account.registered.h1.text">
                <fmt:param value="${user.username}"/>
            </fmt:message>
        </h1>

        <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.guest.account.registered.para.text"/>

        <a class="btn btn-large btn-primary" href="#">
            Lost my activation email !
        </a>

    </s:layout-component>
</s:layout-render>

