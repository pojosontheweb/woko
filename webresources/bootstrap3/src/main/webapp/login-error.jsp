<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="WEB-INF/woko/jsp/taglibs.jsp" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.login.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">

        <div class="container">

            <h1 class="page-header">
                <fmt:message bundle="${wokoBundle}" key="woko.login.failed"/>
            </h1>

            <form method="POST" action="j_security_check" role="form" class="form-horizontal">
                <div class="form-group">
                    <label for="username" class="col-xs-3 control-label"><fmt:message bundle="${wokoBundle}" key="user.username"/></label>
                    <div class="col-xs-9 col-sm-6 col-md-4">
                        <input type="text" name="j_username" id="username" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-xs-3 control-label"><fmt:message bundle="${wokoBundle}" key="user.password"/></label>
                    <div class="col-xs-9 col-sm-6 col-md-4">
                        <input type="password" name="j_password" id="password" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-9 col-xs-offset-3 col-sm-6 col-md-4">
                        <button name="login" type="submit" class="btn btn-primary">
                            <fmt:message bundle="${wokoBundle}" key="login"/>
                        </button>
                    </div>
                </div>
            </form>

        </div>

    </s:layout-component>
</s:layout-render>