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
    String objectKey = "object." + propertyName;
    String propertyLabel = propertyName;
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
%>
<span class="wokoPropertyName">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <s:label for="<%=objectKey%>"><%=propertyLabel%></s:label>
    </span>
</span>


