<%@ page import="woko2.facets.builtin.all.RenderPropertyValue" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute("renderPropertyValue");
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getContext();
    Object propertyValue = fctx.getTargetObject();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    Class propertyClass = Util.getPropertyType(owningObject.getClass(), propertyName);
%>
<span class="wokoPropertyValue">
    <span class="<%=propertyName%> <%=propertyClass.getSimpleName()%>"><c:out value="<%=propertyValue%>"/></span>
</span>


