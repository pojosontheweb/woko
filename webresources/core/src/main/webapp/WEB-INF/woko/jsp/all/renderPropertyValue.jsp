<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.facets.builtin.RenderTitle" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.util.Util" %>

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

<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute(WokoFacets.renderPropertyValue);
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getFacetContext();
    Woko woko = fctx.getWoko();
    ObjectStore os = fctx.getWoko().getObjectStore();
    Object propertyValue = fctx.getTargetObject();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    Class<?> propertyClass = propertyValue!=null ?
            propertyValue.getClass() :
            Util.getPropertyType(owningObject.getClass(), propertyName);

    String propertyMappedClassName = os.getClassMapping(propertyClass);
    String propertyClassName;
    String href = null;
    String linkTitle = null;
    if (propertyMappedClassName!=null) {
        propertyClassName = propertyMappedClassName;
        if (propertyValue!=null) {
            RenderTitle rt = (RenderTitle)woko.getFacet(WokoFacets.renderTitle, request, propertyValue, propertyClass, true);
            linkTitle = rt.getTitle();
            String key = os.getKey(propertyValue);
            if (key!=null) {
                // we have a className and a key, can the user view the object ?
                if (woko.getFacet(WokoFacets.view, request, propertyValue, propertyClass)!=null) {
                    href = new StringBuilder().
                              append(request.getContextPath()).
                              append("/").
                              append(WokoFacets.view).
                              append("/").
                              append(propertyClassName).
                              append("/").
                              append(key).
                              toString();
                }
            }
        }
    } else {
        propertyClassName = propertyClass.getName();
    }
    Object propertyValueStr = linkTitle!=null ? linkTitle : propertyValue;
%>
<span class="wokoPropertyValue">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <c:choose>
            <c:when test="<%=href!=null%>">
                <a href="<%=href%>"><c:out value="<%=linkTitle%>"/></a>
            </c:when>
            <c:otherwise>
                <c:out value="<%=propertyValueStr%>"/>
            </c:otherwise>
        </c:choose>
    </span>
</span>


