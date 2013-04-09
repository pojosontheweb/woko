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
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="woko.facets.builtin.RenderTitle" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%
    RenderTitle renderTitle = (RenderTitle)request.getAttribute(WokoFacets.renderTitleEdit);
    if (renderTitle==null) {
        renderTitle = (RenderTitle)request.getAttribute(RenderTitle.FACET_NAME);
    }
%>
<h1 class="wokoObjectTitle"><%=renderTitle.getTitle()%></h1>