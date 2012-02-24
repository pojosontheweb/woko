<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message var="pageTitle" key="woko.login.error.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">

        <h1 class="page-header"><fmt:message key="woko.login.failed"/> </h1>
        <div class="row">
            <div class="span2">
                <form method="POST" action="j_security_check" class="form-horizontal">
                    <fieldset>
                        <div class="control-group">
                            <label class="control-label" for="username"><fmt:message key="user.username"/></label>
                            <div class="controls">
                                <input type="text" name="j_username" id="username"/>
                            </div>
                        </div>
                        <div class="control-group">
                            <label class="control-label" for="password"><fmt:message key="user.password"/></label>
                            <div class="controls">
                                <input type="password" name="j_password" id="password"/>
                            </div>
                        </div>
                        <div class="form-actions">
                            <button class="btn btn-primary" type="submit"><fmt:message key="login"/></button>
                        </div>
                    </fieldset>
                </form>
            </div>
        </div>
    </s:layout-component>
</s:layout-render>