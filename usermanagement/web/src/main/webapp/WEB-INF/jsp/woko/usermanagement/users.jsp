<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%--
  ~ Copyright 2001-2010 Remi Vankeisbelck
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~       http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

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