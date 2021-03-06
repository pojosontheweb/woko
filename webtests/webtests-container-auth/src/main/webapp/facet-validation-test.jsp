<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%--
  ~ Copyright 2001-2012 Remi Vankeisbelck
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

<html>
<head>
    <script type="text/javascript" src="${pageContext.request.contextPath}/woko/js/woko.base.js"></script>
</head>
<body>
<h1>facet validation test</h1>
<s:messages/>
<s:errors/>
<h2>regular validation</h2>
<s:form action="/testValidate">
    <s:text name="facet.prop"/>
    <s:submit name="doIt"/>
</s:form>
<h2>with @DontValidate</h2>
<s:form action="/multiEventsWithDontValidate">
    <s:hidden name="otherEvent"/>
    <s:text name="facet.myProp"/>
    <s:submit name="doIt2"/>
</s:form>
</body>
</html>
