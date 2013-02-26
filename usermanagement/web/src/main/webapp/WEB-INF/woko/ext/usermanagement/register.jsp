<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.ext.usermanagement.facets.registration.RegisterFragmentGuest" %>
<%@ page import="woko.facets.builtin.Layout" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.register.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.h1.text">
                <fmt:param value="${postRegister.username}"/>
            </fmt:message>
        </h1>

        <div class="row-fluid">
            <div class="span12">
               <w:includeFacet facetName="<%=RegisterFragmentGuest.FACET_NAME%>"/>
            </div>
        </div>

    </s:layout-component>
</s:layout-render>

