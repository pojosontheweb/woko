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
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.*" %>
<%@ page import="java.util.Collections" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%
    RenderPropertiesEdit editProperties = (RenderPropertiesEdit)request.getAttribute(WokoFacets.renderPropertiesEdit);
    List<String> propertyNames = editProperties.getPropertyNames();
    Map<String,Object> propertyValues = editProperties.getPropertyValues();
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)editProperties.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
    ObjectStore os = woko.getObjectStore();
    String mappedClassName = os.getClassMapping(os.getObjectClass(owningObject));
    String formUrl = "/save/" + mappedClassName;
    String key = os.getKey(owningObject);
    if (key!=null) {
        formUrl += "/" + key;
    }
    boolean partial = editProperties.isPartialForm();
    Map<String,Object> hiddenFields = editProperties.getHiddenFields();
    List<String> readOnlyProps = editProperties.getReadOnlyPropertyNames();
    if (readOnlyProps==null) {
        readOnlyProps = Collections.emptyList();
    }
%>
<div class="wokoPropertiesEdit">
    <s:form action="<%=formUrl%>" partial="<%=partial%>">
    <%
        // generate the input fields
        if (hiddenFields!=null) {
            for (String fieldName : hiddenFields.keySet()) {
                Object fieldVal = hiddenFields.get(fieldName);
                if (fieldVal!=null) {
    %>
                    <s:hidden name="<%=fieldName%>"/>
    <%
                } else {
    %>
                    <s:hidden name="<%=fieldName%>" value="<%=fieldVal%>"/>
    <%
                }
            }
        }
    %>
        <table>
            <tbody>
            <%
                for (String pName : propertyNames) {
                    Object pVal = propertyValues.get(pName);

                    RenderPropertyName renderPropertyName =
                        woko.getFacet(WokoFacets.renderPropertyName, request, owningObject, owningObject.getClass(), true);
                    renderPropertyName.setPropertyName(pName);
                    String pNameFragmentPath = renderPropertyName.getFragmentPath(request);

                    RenderPropertyValue editPropertyValue;
                    if (readOnlyProps.contains(pName)) {
                        // read-only view... use renderPropertyValue !
                        editPropertyValue = Util.getRenderPropValueFacet(woko,  request, owningObject, pName, pVal);
                    } else {
                        // editable : use Edit facet
                        editPropertyValue = Util.getRenderPropValueEditFacet(woko, request, owningObject, pName, pVal);
                    }
                    String pValFragmentPath = editPropertyValue.getFragmentPath(request);

                    String prefix = "object";
                    if (editPropertyValue instanceof RenderPropertyValueEdit) {
                        prefix = ((RenderPropertyValueEdit)editPropertyValue).getFieldPrefix();
                    }
                    String fullFieldName = prefix + "." + pName;
            %>
            <tr>
                <th><jsp:include page="<%=pNameFragmentPath%>"/></th>
                <td><jsp:include page="<%=pValFragmentPath%>"/></td>
                <td><s:errors field="<%=fullFieldName%>"/></td>
            </tr>
            <%
                }
            %>
                <c:if test="<%=!partial%>">
                    <tr>
                        <td class="wokoButtonRow" colspan="2">
                            <w:includeFacet facetName="renderPropertiesEditButtons" targetObject="<%=owningObject%>"/>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </s:form>
</div>