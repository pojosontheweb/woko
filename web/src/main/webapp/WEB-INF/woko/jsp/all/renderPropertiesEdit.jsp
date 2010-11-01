<%@ page import="woko2.facets.builtin.RenderProperties" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ page import="woko2.Woko" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.facets.builtin.RenderPropertyName" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ page import="woko2.facets.builtin.RenderPropertyValue" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    RenderProperties editProperties = (RenderProperties)request.getAttribute("renderPropertiesEdit");
    List<String> propertyNames = editProperties.getPropertyNames();
    Map<String,Object> propertyValues = editProperties.getPropertyValues();
    WokoFacetContext fctx = (WokoFacetContext)editProperties.getContext();
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
<div class="wokoProperties">
    <s:form action="<%=formUrl%>">
        <table border="1">
            <tbody>
            <%
                for (String pName : propertyNames) {
                    Object pVal = propertyValues.get(pName);

                    RenderPropertyName renderPropertyName =
                        (RenderPropertyName)woko.getFacet("renderPropertyName", request, owningObject, owningObject.getClass(), true);
                    renderPropertyName.setPropertyName(pName);
                    String pNameFragmentPath = renderPropertyName.getFragmentPath(request);

                    Class<?> pClass;
                    if (pVal!=null) {
                        pClass = pVal.getClass();
                    } else {
                        // get class from property descriptor
                        pClass = Util.getPropertyType(owningObject.getClass(), pName);
                    }
                    RenderPropertyValue editPropertyValue =
                        (RenderPropertyValue)woko.getFacet("renderPropertyValueEdit", request, pVal, pClass);
                    if (editPropertyValue==null) {
                        editPropertyValue = (RenderPropertyValue)woko.getFacet("renderPropertyValue", request, pVal, pClass);
                    }
                    editPropertyValue.setOwningObject(owningObject);
                    editPropertyValue.setPropertyName(pName);
                    String pValFragmentPath = editPropertyValue.getFragmentPath(request);
            %>
            <tr>
                <th><jsp:include page="<%=pNameFragmentPath%>"/></th>
                <td><jsp:include page="<%=pValFragmentPath%>"/></td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <s:submit name="save"/>
    </s:form>
</div>