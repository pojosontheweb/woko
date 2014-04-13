<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.all.RenderPropertyValueEditInput" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    RenderPropertyValueEditInput renderFacets = (RenderPropertyValueEditInput)request.getAttribute(WokoFacets.renderPropertyValueEdit);

    String value = (String)renderFacets.getPropertyValue()!=null?(String)renderFacets.getPropertyValue():"";

    String fullFieldName = renderFacets.getFieldPrefix() + "." + renderFacets.getPropertyName();

    String type = renderFacets.getType();


    StringBuilder tBuilderAttributes = new StringBuilder();
    HashMap<String,String> map = renderFacets.getAttributes();

    for(String key:map.keySet())
    {
        tBuilderAttributes.append(key+ "=\"" + map.get(key) + "\"");
        tBuilderAttributes.append(" ");
    }


%>
<input type="<%=type%>"
       class="form-control"
       name="<%=fullFieldName%>"
       value="<%=value%>" <%=tBuilderAttributes.toString()%> />