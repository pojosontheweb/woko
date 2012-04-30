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
<%@ page import="woko.facets.builtin.RenderProperties" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.builtin.RenderPropertyName" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%
    RenderProperties editProperties = (RenderProperties)request.getAttribute(WokoFacets.renderPropertiesEdit);
    List<String> propertyNames = editProperties.getPropertyNames();
    Map<String,Object> propertyValues = editProperties.getPropertyValues();
    WokoFacetContext fctx = (WokoFacetContext)editProperties.getFacetContext();
    Woko woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
    ObjectStore os = woko.getObjectStore();
    String mappedClassName = os.getClassMapping(owningObject.getClass());
    String formUrl = "/save/" + mappedClassName;
    String key = os.getKey(owningObject);
    if (key!=null) {
        formUrl += "/" + key;
    }
%>
<div class="wokoPropertiesEdit">
    <s:form action="<%=formUrl%>">
        <table>
            <tbody>
            <%
                for (String pName : propertyNames) {
                    Object pVal = propertyValues.get(pName);

                    RenderPropertyName renderPropertyName =
                        (RenderPropertyName)woko.getFacet(WokoFacets.renderPropertyName, request, owningObject, owningObject.getClass(), true);
                    renderPropertyName.setPropertyName(pName);
                    String pNameFragmentPath = renderPropertyName.getFragmentPath(request);

                    RenderPropertyValue editPropertyValue = Util.getRenderPropValueEditFacet(woko, request, owningObject, pName, pVal);
                    String pValFragmentPath = editPropertyValue.getFragmentPath(request);

                    String fullFieldName = "object." + pName;
            %>
            <tr>
                <th><jsp:include page="<%=pNameFragmentPath%>"/></th>
                <td><jsp:include page="<%=pValFragmentPath%>"/></td>
                <td><s:errors field="<%=fullFieldName%>"/></td>
            </tr>
            <%
                }
            %>
                <tr><td class="wokoButtonRow" colspan="2"><s:submit name="save"/></td></tr>
            </tbody>
        </table>
    </s:form>
</div>