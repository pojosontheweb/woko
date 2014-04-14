<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.*" %>
<%@ page import="java.util.Collections" %>

<%
    RenderPropertiesEdit editProperties = (RenderPropertiesEdit)request.getAttribute(WokoFacets.renderPropertiesEdit);
    List<String> propertyNames = editProperties.getPropertyNames();
    Map<String,Object> propertyValues = editProperties.getPropertyValues();
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)editProperties.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
    ObjectStore os = woko.getObjectStore();
    String mappedClassName = os.getClassMapping(os.getObjectClass(owningObject));
    String formUrl = "/save/" + mappedClassName;
    String key = os.getKey(owningObject);
    if (key!=null) {
        formUrl += "/" + key;
    }
    boolean partial = editProperties.isPartialForm();

    Map<String,Object> hiddenFields = editProperties.getHiddenFields();
    List<String> readOnlyProps = editProperties.getReadOnlyPropertyNames();
    if (readOnlyProps==null) {
        readOnlyProps = Collections.emptyList();
    }
%>
<s:form action="<%=formUrl%>" class="form-horizontal" partial="<%=partial%>">
<%
    // generate the hidden input fields if needed
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
<div class="container w-properties">
<%
    for (String pName : propertyNames) {
        Object pVal = propertyValues.get(pName);

        RenderPropertyName renderPropertyName =
            woko.getFacet(WokoFacets.renderPropertyName, request, owningObject, owningObject.getClass(), true);
        renderPropertyName.setPropertyName(pName);
        String pNameFragmentPath = renderPropertyName.getFragmentPath(request);

        RenderPropertyValue editPropertyValue;
        if (readOnlyProps.contains(pName)) {
            // read-only view... use renderPropertyValue !
            editPropertyValue = Util.getRenderPropValueFacet(woko,  request, owningObject, pName, pVal);
        } else {
            // editable : use Edit facet
            editPropertyValue = Util.getRenderPropValueEditFacet(woko, request, owningObject, pName, pVal);
        }
        String pValFragmentPath = editPropertyValue.getFragmentPath(request);
        String prefix = "object";
        if (editPropertyValue instanceof RenderPropertyValueEdit) {
            prefix = ((RenderPropertyValueEdit)editPropertyValue).getFieldPrefix();
        }

        String fullFieldName = prefix + "." + pName;

        Class<?> pType = Util.getPropertyType(owningObject.getClass(), pName);
        String pTypeStr = woko.getObjectStore().getClassMapping(pType);
%>
        <c:set var="fullFieldNameStripes" value="<%=fullFieldName%>"/>
        <div class="w-property <%=pName%> <%=pTypeStr%> form-group ${empty(actionBean.context.validationErrors[fullFieldNameStripes]) ? '' : 'has-error'} ">
            <div class="w-property-name col-lg-2 col-sm-3">
                <jsp:include page="<%=pNameFragmentPath%>"/>
            </div>
            <div class="w-property-value col-lg-10 col-sm-9">
                <jsp:include page="<%=pValFragmentPath%>"/>
                <s:errors field="<%=fullFieldName%>"/>
            </div>
        </div>
<%
    }
%>
    <c:if test="<%=!partial%>">
        <w:includeFacet facetName="renderPropertiesEditButtons" targetObject="<%=owningObject%>"/>
    </c:if>
</div>
</s:form>
