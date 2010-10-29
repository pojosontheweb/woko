<%@ page import="woko2.facets.builtin.all.RenderPropertyValue" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute("renderPropertyValue");
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getContext();
    ObjectStore os = fctx.getWoko().getObjectStore();
    Object propertyValue = fctx.getTargetObject();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
%>
<span class="wokoPropertyValue">
    <span class="<%=propertyName%> <%=propertyClassName%>"><c:out value="<%=propertyValue%>"/></span>
</span>


