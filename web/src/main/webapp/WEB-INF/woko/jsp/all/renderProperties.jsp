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
    RenderProperties renderProperties = (RenderProperties)request.getAttribute("renderProperties");
    List<String> propertyNames = renderProperties.getPropertyNames();
    Map<String,Object> propertyValues = renderProperties.getPropertyValues();
    WokoFacetContext fctx = (WokoFacetContext)renderProperties.getContext();
    Woko woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
%>
<div class="wokoProperties">
    <table>
        <tbody>
        <%
            for (String pName : propertyNames) {
                Object pVal = propertyValues.get(pName);

                RenderPropertyName renderPropertyName =
                    (RenderPropertyName)woko.getFacet("renderPropertyName", request, owningObject, owningObject.getClass(), true);
                renderPropertyName.setPropertyName(pName);
                String pNameFragmentPath = renderPropertyName.getFragmentPath(request);

                RenderPropertyValue renderPropertyValue = Util.getRenderPropValueFacet(woko, request, owningObject, pName, pVal);
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