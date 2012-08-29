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
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute(WokoFacets.renderPropertyValueEdit);
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getFacetContext();
    ObjectStore os = fctx.getWoko().getObjectStore();
    String propertyName = renderPropertyValue.getPropertyName();
    String propertyClassName = os.getClassMapping(renderPropertyValue.getPropertyType());
    Object propVal = renderPropertyValue.getPropertyValue();
    String fullFieldName = "object." + propertyName;
    String emptyOptSelected = propVal==null ? "true" : "false";
%>
<span class="wokoPropertyValueEdit">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <s:select name="<%=fullFieldName%>">
            <s:option value="" selected="<%=emptyOptSelected%>"/>
            <s:options-enumeration enum="<%=renderPropertyValue.getPropertyType().getName()%>"/>
        </s:select>
    </span>
</span>


