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
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.all.RenderPropertyValueEditStripesText" %>
<%
    RenderPropertyValueEditStripesText<?,?,?,?> renderPropertyValue =
            (RenderPropertyValueEditStripesText<?,?,?,?>)request.getAttribute(WokoFacets.renderPropertyValueEdit);
    String propertyName = renderPropertyValue.getPropertyName();
    String fullFieldName = renderPropertyValue.getFieldPrefix() + "." + propertyName;
    if (renderPropertyValue.isTextArea()) {
%>
        <s:textarea name="<%=fullFieldName%>" class="form-control" />
<%
    } else {
        // special handling for numbers
        Class<?> propertyType = renderPropertyValue.getPropertyType();
        String css = "col-xs-12 col-sm-10 col-lg-6";
        if (Number.class.isAssignableFrom(propertyType)) {
            css = "col-xs-12 col-sm-6 col-md-4";
        }
%>
        <div class="row">
            <div class="<%=css%>">
                <s:text name="<%=fullFieldName%>" class="form-control"/>
            </div>
        </div>
<% } %>


