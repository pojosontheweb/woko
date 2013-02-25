<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="java.util.Locale" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.RenderPropertyValueEdit" %>

<%
    RenderPropertyValueEdit renderPropertyValue = (RenderPropertyValueEdit)request.getAttribute(WokoFacets.renderPropertyValueEdit);
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext<?,?,?,?>)renderPropertyValue.getFacetContext();
    ObjectStore os = fctx.getWoko().getObjectStore();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    String owningClass = os.getClassMapping(owningObject.getClass());
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
    String fullFieldName = renderPropertyValue.getFieldPrefix() + "." + propertyName;
    String fieldId = "dp-" + owningClass + "-" + propertyName;
    Locale locale = request.getLocale();
    String localeStr = locale!=null ? locale.toString() : "";
%>
<span class="wokoPropertyValueEdit">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <div class="input-prepend">
            <span class="add-on"><i class="icon-calendar"></i></span>
            <s:text name="<%=fullFieldName%>" rel="datepicker" id="<%=fieldId%>" class="input-medium"/>
        </div>
    </span>
</span>
