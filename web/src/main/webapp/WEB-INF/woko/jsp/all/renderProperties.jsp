<%@ page import="woko2.facets.builtin.all.RenderProperties" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ page import="woko2.Woko" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.facets.builtin.all.RenderPropertyName" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ page import="woko2.facets.builtin.all.RenderPropertyValue" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    RenderProperties renderProperties = (RenderProperties)request.getAttribute("renderProperties");
    List<String> propertyNames = renderProperties.getPropertyNames();
    Map<String,Object> propertyValues = renderProperties.getPropertyValues();
    WokoFacetContext fctx = (WokoFacetContext)renderProperties.getContext();
    Woko woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
%>
<div class="wokoProperties">
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
                RenderPropertyValue renderPropertyValue =
                    (RenderPropertyValue)woko.getFacet("renderPropertyValue", request, pVal, pClass, true);
                renderPropertyValue.setOwningObject(owningObject);
                renderPropertyValue.setPropertyName(pName);
                String pValFragmentPath = renderPropertyValue.getFragmentPath(request);
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
</div>