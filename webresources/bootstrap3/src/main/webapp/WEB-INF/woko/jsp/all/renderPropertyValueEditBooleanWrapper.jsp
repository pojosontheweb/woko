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
<%@ page import="woko.facets.builtin.RenderPropertyValueEdit" %>
<%
    RenderPropertyValueEdit renderPropertyValue = (RenderPropertyValueEdit)request.getAttribute(WokoFacets.renderPropertyValueEdit);
    String propertyName = renderPropertyValue.getPropertyName();
    String fullFieldName = renderPropertyValue.getFieldPrefix() + "." + propertyName;
%>
<div class="row">
    <div class="col-md-2 col-sm-3 col-xs-4">
        <s:select name="<%=fullFieldName%>" class="form-control">
            <s:option value="">&nbsp;</s:option>
            <s:option value="true">true</s:option>
            <s:option value="false">false</s:option>
        </s:select>
    </div>
</div>

