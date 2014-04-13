<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="WEB-INF/woko/jsp/taglibs.jsp" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.login.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.login.title"/>
        </h1>

        <form method="POST" action="j_security_check" role="form">
            <div class="form-group">
                <label for="username"><fmt:message bundle="${wokoBundle}" key="user.username"/></label>
                <input type="text" name="j_username" id="username" class="form-control"/>
            </div>
            <div class="form-group">
                <label for="password"><fmt:message bundle="${wokoBundle}" key="user.password"/></label>
                <input type="password" name="j_password" id="password" class="form-control"/>
            </div>
            <button name="login" type="submit" class="btn btn-primary">
                <fmt:message bundle="${wokoBundle}" key="login"/>
            </button>
        </form>

    </s:layout-component>
</s:layout-render>