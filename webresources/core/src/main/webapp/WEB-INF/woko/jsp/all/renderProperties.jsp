<%@ page import="woko.facets.builtin.RenderProperties" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.builtin.RenderPropertyName" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    RenderProperties renderProperties = (RenderProperties)request.getAttribute(WokoFacets.renderProperties);
    List<String> propertyNames = renderProperties.getPropertyNames();
    Map<String,Object> propertyValues = renderProperties.getPropertyValues();
    WokoFacetContext fctx = (WokoFacetContext)renderProperties.getFacetContext();
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
                    (RenderPropertyName)woko.getFacet(WokoFacets.renderPropertyName, request, owningObject, owningObject.getClass(), true);
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