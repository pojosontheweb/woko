<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.facets.builtin.RenderPropertyName" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    RenderPropertyName renderPropertyName = (RenderPropertyName)request.getAttribute("renderPropertyName");
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyName.getFacetContext();
    Woko woko = fctx.getWoko();
    ObjectStore os = woko.getObjectStore();
    String propertyName = renderPropertyName.getPropertyName();
    Object owningObject = fctx.getTargetObject();
    String owningClassAsKey = owningObject.getClass().getSimpleName() + "." + propertyName;
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
%>
<span class="wokoPropertyName">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <s:label for="<%=owningClassAsKey%>"/>
    </span>
</span>


