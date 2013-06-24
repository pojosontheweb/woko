<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.login.error.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">

        <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.login.failed"/> </h1>
        <form method="POST" action="j_security_check" class="form-horizontal">
            <fieldset>
                <div class="pure-control-group">
                    <label class="control-label" for="username"><fmt:message bundle="${wokoBundle}" key="user.username"/></label>
                    <div class="pure-controls">
                        <input type="text" name="j_username" id="username"/>
                    </div>
                </div>
                <div class="pure-control-group">
                    <label class="control-label" for="password"><fmt:message bundle="${wokoBundle}" key="user.password"/></label>
                    <div class="pure-controls">
                        <input type="password" name="j_password" id="password"/>
                    </div>
                </div>
                <div class="form-actions">
                    <button name="login" class="pure-button pure-button-primary pure-button-large" type="submit"><fmt:message bundle="${wokoBundle}" key="login"/></button>
                </div>
            </fieldset>
        </form>

    </s:layout-component>
</s:layout-render>