<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.builtin.*" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.post.register.page.title"/>
<c:set var="user" value="${postRegister.facetContext.targetObject}"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.post.register.h1.text">
                <fmt:param value="${user.username}"/>
            </fmt:message>
        </h1>

        <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.post.register.para.text">
            <fmt:param value="${user.email}"/>
        </fmt:message>

        <a class="btn btn-large btn-primary" href="${pageContext.request.contextPath}/view/${postRegister.userClassName}/${user.id}">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.post.register.account.link.text"/>
        </a>

    </s:layout-component>
</s:layout-render>

