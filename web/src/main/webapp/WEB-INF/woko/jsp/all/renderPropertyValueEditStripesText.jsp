<%@ page import="woko2.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ page import="woko2.facets.builtin.RenderPropertyValueEdit" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute(RenderPropertyValueEdit.name);
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getFacetContext();
    ObjectStore os = fctx.getWoko().getObjectStore();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
    String fullFieldName = "object." + propertyName;
%>
<span class="wokoPropertyValueEdit">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <s:text name="<%=fullFieldName%>"/>
    </span>
</span>


