<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.actions.auth.builtin.WokoLogin" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.login.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">

        <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.login.title"/></h1>
        <div class="row">
            <div class="span2">
                <s:form beanclass="<%=WokoLogin.class%>" class="form-horizontal">
                    <s:hidden name="targetUrl"/>
                    <fieldset>
                        <div class="control-group">
                            <s:label for="user.username" class="control-label"/>
                            <div class="controls">
                                <s:text name="username" id="username"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <s:label for="user.password" class="control-label"/>
                            <div class="controls">
                                <s:password name="password" id="password"/>
                            </div>
                        </div>
                        <div class="form-actions">
                            <s:submit class="btn btn-primary" name="login"/>
                        </div>
                    </fieldset>
                </s:form>
            </div>
        </div>

    </s:layout-component>
</s:layout-render>