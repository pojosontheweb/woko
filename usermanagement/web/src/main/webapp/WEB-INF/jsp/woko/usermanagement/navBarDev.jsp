<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

<ul>
    <li><a href="${pageContext.request.contextPath}/home"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.home"/> </a></li>
    <li><a href="${pageContext.request.contextPath}/find"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.find"/> </a></li>
    <li><a href="${pageContext.request.contextPath}/create"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.create"/> </a></li>
    <li><a href="${pageContext.request.contextPath}/studio"><fmt:message bundle="${wokoBundle}" key="woko.devel.navbar.studio"/> </a></li>
    <li><a href="${pageContext.request.contextPath}/users">users</a></li>
</ul>