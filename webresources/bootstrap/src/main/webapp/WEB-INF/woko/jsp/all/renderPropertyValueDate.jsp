<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="java.util.Date" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute(WokoFacets.renderPropertyValue);
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getFacetContext();
    ObjectStore os = fctx.getWoko().getObjectStore();
    Date propertyValue = (Date)fctx.getTargetObject();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
%>
<span class="wokoPropertyValue">
    <span class="<%=propertyName%> <%=propertyClassName%>"><fmt:formatDate value="<%=propertyValue%>"/></span>
</span>


