<%@ tag import="woko.facets.FragmentFacet" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="targetObject" required="false" type="java.lang.Object" %>
<%@ attribute name="targetObjectClass" required="false" type="java.lang.Class" %>
<%@ attribute name="facetName" required="false" type="java.lang.String" %>
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

<w:facet targetObject="${targetObject}"
         targetObjectClass="${targetObjectClass}"
         facetName="${facetName}"/>
<%
    FragmentFacet ff = (FragmentFacet)request.getAttribute(facetName);
    String path = ff.getFragmentPath(request);
%>
<jsp:include page="<%=path%>"/>