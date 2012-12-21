<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.*" %>

<%
    RenderPropertiesEdit editProperties = (RenderPropertiesEdit)request.getAttribute(WokoFacets.renderPropertiesEdit);
    List<String> propertyNames = editProperties.getPropertyNames();
    Map<String,Object> propertyValues = editProperties.getPropertyValues();
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)editProperties.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
    ObjectStore os = woko.getObjectStore();
    String mappedClassName = os.getClassMapping(owningObject.getClass());
    String formUrl = "/save/" + mappedClassName;
    String key = os.getKey(owningObject);
    if (key!=null) {
        formUrl += "/" + key;
    }
    boolean partial = editProperties.isPartialForm();

    Map<String,Object> hiddenFields = editProperties.getHiddenFields();
%>
<s:form action="<%=formUrl%>" class="form-horizontal" partial="<%=partial%>">
<%
    // generate the input fields
    if (hiddenFields!=null) {
        for (String fieldName : hiddenFields.keySet()) {
            Object fieldVal = hiddenFields.get(fieldName);
            if (fieldVal!=null) {
%>
                <s:hidden name="<%=fieldName%>"/>
<%
            } else {
%>
                <s:hidden name="<%=fieldName%>" value="<%=fieldVal%>"/>
<%
            }
        }
    }
%>
<fieldset>
<%
    for (String pName : propertyNames) {
        Object pVal = propertyValues.get(pName);

        RenderPropertyName renderPropertyName =
            woko.getFacet(WokoFacets.renderPropertyName, request, owningObject, owningObject.getClass(), true);
        renderPropertyName.setPropertyName(pName);
        String pNameFragmentPath = renderPropertyName.getFragmentPath(request);

        RenderPropertyValue editPropertyValue = Util.getRenderPropValueEditFacet(woko, request, owningObject, pName, pVal);
        String pValFragmentPath = editPropertyValue.getFragmentPath(request);

        String prefix = "object";
        if (editPropertyValue instanceof RenderPropertyValueEdit) {
            prefix = ((RenderPropertyValueEdit)editPropertyValue).getFieldPrefix();
        }

        String fullFieldName = prefix + "." + pName;
%>
        <c:set var="fullFieldNameStripes" value="<%=fullFieldName%>"/>
        <div class="control-group ${empty(actionBean.context.validationErrors[fullFieldNameStripes]) ? '' : 'error'} ">
            <jsp:include page="<%=pNameFragmentPath%>"/>
            <div class="controls">
                <jsp:include page="<%=pValFragmentPath%>"/>
                <s:errors field="<%=fullFieldName%>"/>
            </div>
        </div>
<%
    }
%>
    <c:if test="<%=!partial%>">
        <div class="form-actions">
            <s:submit name="save" class="btn btn-primary btn-large"/>
        </div>
    </c:if>
</fieldset>
</s:form>
