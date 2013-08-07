<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.actions.auth.builtin.WokoLogin" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.login.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">

        <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.login.title"/></h1>
        <s:form beanclass="<%=WokoLogin.class%>" class="pure-form pure-form-stacked">
            <s:hidden name="targetUrl"/>
            <fieldset>
                <div class="pure-control-group">
                    <s:label for="user.username" class="control-label"/>
                    <div class="pure-controls">
                        <s:text name="username" id="username" class="pure-input-1-3"/>
                    </div>
                </div>
                <div class="pure-control-group">
                    <s:label for="user.password" class="control-label"/>
                    <div class="pure-controls">
                        <s:password name="password" id="password" class="pure-input-1-3"/>
                    </div>
                </div>
                <div>
                    <s:submit class="pure-button pure-button-primary pure-button-large" name="login"/>
                </div>
            </fieldset>
        </s:form>

    </s:layout-component>
</s:layout-render>