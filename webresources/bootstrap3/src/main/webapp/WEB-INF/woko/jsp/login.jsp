<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.actions.auth.builtin.WokoLogin" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.login.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">

        <div class="container">
            <h1 class="page-header">
                <fmt:message bundle="${wokoBundle}" key="woko.login.title"/>
            </h1>

            <s:form beanclass="<%=WokoLogin.class%>" class="form-horizontal" role="form">
                <s:hidden name="targetUrl"/>

                <div class="form-group">
                    <s:label for="user.username" class="col-xs-3 control-label"/>
                    <div class="col-xs-9 col-sm-6 col-md-4">
                        <s:text name="username" id="username" class="form-control"/>
                    </div>
                </div>

                <div class="form-group">
                    <s:label for="user.password" class="col-xs-3 control-label"/>
                    <div class="col-xs-9 col-sm-6 col-md-4">
                        <s:password name="password" id="password" class="form-control"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-xs-9 col-xs-offset-3 col-sm-6 col-md-4">
                        <s:submit class="btn btn-primary" name="login"/>
                    </div>
                </div>

            </s:form>
        </div>

    </s:layout-component>
</s:layout-render>