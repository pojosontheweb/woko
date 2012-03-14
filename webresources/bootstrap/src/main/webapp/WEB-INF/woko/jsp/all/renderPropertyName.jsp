<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.facets.builtin.RenderPropertyName" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<%
    RenderPropertyName renderPropertyName = (RenderPropertyName)request.getAttribute(WokoFacets.renderPropertyName);
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyName.getFacetContext();
    Woko woko = fctx.getWoko();
    ObjectStore os = woko.getObjectStore();
    String propertyName = renderPropertyName.getPropertyName();
    Object owningObject = fctx.getTargetObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
    String labelClass = "control-label wokoPropertyName " + propertyClassName + "-" + propertyName;
    String label = "object." + propertyName;
    String labelMsgKey = os.getClassMapping(owningObject.getClass()) + "." + propertyName;

%>
<s:label for="<%=label%>" class="<%=labelClass%>"><fmt:message key="<%=labelMsgKey%>"/></s:label>


