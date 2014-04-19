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
<div class="row">
    <div class="col-xs-12 col-sm-6 col-md-4">
        <div class="input-group">
            <s:text name="<%=fullFieldName%>" rel="datepicker" id="<%=fieldId%>" class="form-control"/>
            <span class="input-group-addon">
                <i class="glyphicon glyphicon-calendar"> </i>
            </span>
        </div>
    </div>
</div>