<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="WEB-INF/woko/jsp/taglibs.jsp" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.login.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">

        <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.login.title"/></h1>
        <form method="POST" action="j_security_check" class="form-horizontal">
            <fieldset>
                <div>
                    <label class="control-label" for="username"><fmt:message bundle="${wokoBundle}" key="user.username"/></label>
                    <div class="pure-controls">
                        <input type="text" name="j_username" id="username" class="pure-input-1-1"/>
                    </div>
                </div>
                <div class="pure-control-group">
                    <label class="control-label" for="password"><fmt:message bundle="${wokoBundle}" key="user.password"/></label>
                    <div class="pure-controls">
                        <input type="password" name="j_password" id="password" class="pure-input-1-1"/>
                    </div>
                </div>
                <div class="form-actions">
                    <button name="login" class="pure-button pure-button-primary  pure-button-large" type="submit"><fmt:message bundle="${wokoBundle}" key="login"/></button>
                </div>
            </fieldset>
        </form>

    </s:layout-component>
</s:layout-render>