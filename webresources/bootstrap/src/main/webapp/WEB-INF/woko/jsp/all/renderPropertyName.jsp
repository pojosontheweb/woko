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
    String objectKey = "object." + propertyName;
    String propertyLabel = propertyName;
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
    String labelClass = "control-label wokoPropertyName " + propertyClassName + "-" + propertyName;
%>
<s:label for="<%=objectKey%>" class="<%=labelClass%>"><%=propertyLabel%></s:label>


