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
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.find.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <h1><fmt:message bundle="${wokoBundle}" key="woko.devel.find.fullText"/> </h1>
        <s:form action="/search">
            <fmt:message bundle="${wokoBundle}" key="woko.devel.find.enterQuery"/>
            <s:text name="facet.query"/>
            <s:submit name="search"/>
        </s:form>
        <h1><fmt:message bundle="${wokoBundle}" key="woko.devel.find.byClass"/> </h1>
        <p>
            <fmt:message bundle="${wokoBundle}" key="woko.devel.find.selectName"/>
        </p>
        <ul>
            <c:forEach items="${find.mappedClasses}" var="className">
                <li><a href="${pageContext.request.contextPath}/list/${className}">${className}</a></li>    
            </c:forEach>
        </ul>
    </s:layout-component>
</s:layout-render>