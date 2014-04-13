<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.RenderPropertyValueEdit" %>

<%
    RenderPropertyValueEdit renderPropertyValue = (RenderPropertyValueEdit)request.getAttribute(WokoFacets.renderPropertyValueEdit);
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext<?,?,?,?>)renderPropertyValue.getFacetContext();
    ObjectStore os = fctx.getWoko().getObjectStore();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    Class<?> oClass = os.getObjectClass(owningObject);
    String owningClass = os.getClassMapping(oClass);
    String fullFieldName = renderPropertyValue.getFieldPrefix() + "." + propertyName;
    String fieldId = "dp-" + owningClass + "-" + propertyName;
%>
<s:text name="<%=fullFieldName%>" rel="datepicker" id="<%=fieldId%>" class="form-control"/>