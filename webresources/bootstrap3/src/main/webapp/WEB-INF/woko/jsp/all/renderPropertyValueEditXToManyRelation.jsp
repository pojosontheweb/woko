<%--
  ~ Copyright 2001-2012 Remi Vankeisbelck
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.RenderPropertyValueEdit" %>
<%@ page import="woko.Woko" %>
<%@ page import="java.util.Collection" %>
<%@ page import="woko.facets.builtin.all.RenderPropertyValueEditXToManyRelation" %>
<%
    RenderPropertyValueEditXToManyRelation renderPropertyValue = (RenderPropertyValueEditXToManyRelation)request.getAttribute(RenderPropertyValueEdit.FACET_NAME);
    WokoFacetContext<?,?,?,?> fctx = renderPropertyValue.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    ObjectStore os = woko.getObjectStore();
    String propertyName = renderPropertyValue.getPropertyName();
    String fullFieldName = renderPropertyValue.getFieldPrefix() + "." + propertyName;
    Collection<?> propVal = (Collection<?>)renderPropertyValue.getPropertyValue();
%>
<select name="<%=fullFieldName%>" multiple="multiple" class="form-control">
    <%
        Collection<?> choices = renderPropertyValue.getChoices();
        for (Object choice : choices) {
            String key = os.getKey(choice);
            String title = Util.getTitle(request, choice);
            String selected = propVal!=null && propVal.contains(choice) ?
                    "selected=\"selected\"" :
                    "";
    %>
        <option value="<%=key%>" <%=selected%> ><c:out value="<%=title%>"/></option>
    <%
        }
    %>
</select>

