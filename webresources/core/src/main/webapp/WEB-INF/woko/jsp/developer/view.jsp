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
<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="<%=WokoFacets.layout%>" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>
<c:set var="pageTitle" value="${renderTitle.title}"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <w:includeFacet facetName="<%=WokoFacets.renderObject%>" targetObject="${o}"/>
    </s:layout-component>
</s:layout-render>

