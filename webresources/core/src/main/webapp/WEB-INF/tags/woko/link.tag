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
<%@ tag import="woko.facets.builtin.WokoFacets" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ attribute name="object" required="true" type="java.lang.Object" %>
<%@ attribute name="facetName" required="false" type="java.lang.String" %>
<c:if test="facetName==null">
    <c:set var="facetName" value="<%=WokoFacets.view%>"/>
</c:if>
<w:url object="${object}" var="objectUrl" facetName="${facetName}"/>
<w:facet facetName="<%=WokoFacets.renderTitle%>" targetObject="${object}"/>
<a href="${objectUrl}"><c:out value="${renderTitle.title}"/></a>