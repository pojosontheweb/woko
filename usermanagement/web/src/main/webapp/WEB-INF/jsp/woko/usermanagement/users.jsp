<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="users">
    <s:layout-component name="body">
        <h1>User management</h1>
        <p>
            This page allows privileged users (developer role by default) to
            manage the users of the application.
        </p>
        <p>
            User manager : <strong>${users.userManager}</strong>
        </p>
        <h2>Users in the database</h2>


    </s:layout-component>
</s:layout-render>