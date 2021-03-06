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
<%@ page import="woko.facets.builtin.RenderPropertiesBefore" %>
<%@ page import="woko.facets.builtin.RenderPropertiesAfter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<c:set var="o" value="${renderObject.facetContext.targetObject}"/>
<div class="wokoObject">
    <w:includeFacet targetObject="${o}" facetName="<%=WokoFacets.renderLinks%>"/>
    <w:includeFacet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>
    <w:includeFacet targetObject="${o}"
                    facetName="<%=RenderPropertiesBefore.FACET_NAME%>"
                    throwIfNotFound="false"/>
    <w:includeFacet targetObject="${o}" facetName="<%=WokoFacets.renderProperties%>"/>
    <w:includeFacet targetObject="${o}"
                    facetName="<%=RenderPropertiesAfter.FACET_NAME%>"
                    throwIfNotFound="false"/>
</div>