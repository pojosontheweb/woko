<%@ page import="woko.facets.builtin.RenderProperties" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.builtin.RenderPropertyName" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%
    RenderProperties editProperties = (RenderProperties)request.getAttribute("renderPropertiesEdit");
    List<String> propertyNames = editProperties.getPropertyNames();
    Map<String,Object> propertyValues = editProperties.getPropertyValues();
    WokoFacetContext fctx = (WokoFacetContext)editProperties.getFacetContext();
    Woko woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
    ObjectStore os = woko.getObjectStore();
    String mappedClassName = os.getClassMapping(owningObject.getClass());
    String formUrl = "/save/" + mappedClassName;
    String key = os.getKey(owningObject);
    if (key!=null) {
        formUrl += "/" + key;
    }
%>
<s:form action="<%=formUrl%>" class="form-horizontal">
<fieldset>
<%
    for (String pName : propertyNames) {
        Object pVal = propertyValues.get(pName);

        RenderPropertyName renderPropertyName =
            (RenderPropertyName)woko.getFacet("renderPropertyName", request, owningObject, owningObject.getClass(), true);
        renderPropertyName.setPropertyName(pName);
        String pNameFragmentPath = renderPropertyName.getFragmentPath(request);

        RenderPropertyValue editPropertyValue = Util.getRenderPropValueEditFacet(woko, request, owningObject, pName, pVal);
        String pValFragmentPath = editPropertyValue.getFragmentPath(request);

        String fullFieldName = "object." + pName;
%>
        <div class="control-group">
            <jsp:include page="<%=pNameFragmentPath%>"/>
            <div class="controls">
                <jsp:include page="<%=pValFragmentPath%>"/>
                <s:errors field="<%=fullFieldName%>"/>
            </div>
        </div>
<%
    }
%>
    <div class="form-actions">
        <s:submit name="save" class="btn btn-primary btn-large"/>
    </div>
</fieldset>
</s:form>
