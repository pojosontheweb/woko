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
<%@ page import="woko.facets.builtin.RenderPropertyName" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="net.sourceforge.stripes.controller.StripesFilter" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="woko.facets.builtin.RenderPropertiesEdit" %>
<%
    RenderPropertyName renderPropertyName = (RenderPropertyName)request.getAttribute(WokoFacets.renderPropertyName);
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)renderPropertyName.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    ObjectStore os = woko.getObjectStore();
    String propertyName = renderPropertyName.getPropertyName();
    Object owningObject = fctx.getTargetObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(os.getObjectClass(owningObject), propertyName));

    // check in request if we have a renderPropertiesEdit facet that we could
    // use for prefix
    String prefix = "object";
    RenderPropertiesEdit rpe = (RenderPropertiesEdit)request.getAttribute(RenderPropertiesEdit.FACET_NAME);
    if (rpe!=null) {
        prefix = rpe.getFieldPrefix();
    }
    String label = prefix + "." + propertyName;
    String labelMsgKey = os.getClassMapping(os.getObjectClass(owningObject)) + "." + propertyName;
    ResourceBundle b = StripesFilter.getConfiguration().
            getLocalizationBundleFactory().getFormFieldBundle(request.getLocale());
    String msg = propertyName;
    try {
        msg = b.getString(labelMsgKey);
    } catch(MissingResourceException e) {
        // just let it through and use property name
    }
%>
<span class="wokoPropertyName">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <s:label for="<%=label%>"><c:out value="<%=msg%>"/></s:label>
    </span>
</span>


