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
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="<%=WokoFacets.layout%>" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${renderTitle.title}">
    <s:layout-component name="body">
        <h1><fmt:message bundle="${wokoBundle}" key="woko.devel.confirmDelete.title"/></h1>
        <p>

            <fmt:message bundle="${wokoBundle}" key="woko.devel.confirmDelete.question">
                <fmt:param><w:title object="${o}"/></fmt:param>
            </fmt:message>
            
            <w:objectClassName var="className" object="${actionBean.object}"/>
            <w:objectKey var="key" object="${actionBean.object}"/>
        </p>

        <s:form action="${pageContext.request.contextPath}/delete/${className}/${key}">
            <s:submit name="facet.confirm"/>
            <s:submit name="facet.cancel"/>
        </s:form>

    </s:layout-component>
</s:layout-render>
