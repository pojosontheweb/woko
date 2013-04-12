<%--
  ~ Copyright 2001-2013 Remi Vankeisbelck
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
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.ext.tinymce.RenderPropertyValueEditTinyMCE4" %>
<%
    RenderPropertyValueEditTinyMCE4<?,?,?,?> renderPropertyValue =
            (RenderPropertyValueEditTinyMCE4<?,?,?,?>)request.getAttribute(WokoFacets.renderPropertyValueEdit);
    WokoFacetContext<?,?,?,?> fctx = renderPropertyValue.getFacetContext();
    ObjectStore os = fctx.getWoko().getObjectStore();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
    String fullFieldName = renderPropertyValue.getFieldPrefix() + "." + propertyName;
    String textAreaId = renderPropertyValue.getTextAreaId();
%>
<span class="wokoPropertyValueEdit">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <s:textarea id="<%=textAreaId%>" name="<%=fullFieldName%>"/>
    </span>
</span>
<w:cacheToken paramName="cacheToken" tokenValue="cacheTokenValue"/>
<c:set var="cacheTokenParams" value="${cacheToken}=${cacheTokenValue}"/>
<script type="text/javascript" src="${cp}/tiny_mce4/tinymce.min.js?${cacheTokenParams}"></script>
<script type="text/javascript">
    $(function() {

        debugger;

        tinyMCE.init({
                // General options
                selector: "#<%=textAreaId%>",
                plugins : [
                    "advlist autolink lists link image charmap print preview anchor",
                    "searchreplace visualblocks code fullscreen",
                    "insertdatetime media table contextmenu paste"
                ]
        });

    });
</script>


