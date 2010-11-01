<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ page import="woko2.facets.builtin.RenderPropertyName" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.Woko" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    RenderPropertyName renderPropertyName = (RenderPropertyName)request.getAttribute("renderPropertyName");
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyName.getContext();
    Woko woko = fctx.getWoko();
    ObjectStore os = woko.getObjectStore();
    String propertyName = renderPropertyName.getPropertyName();
    Object owningObject = fctx.getTargetObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
%>
<span class="wokoPropertyName">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <c:out value="${renderPropertyName.propertyName}"/>
    </span>
</span>


