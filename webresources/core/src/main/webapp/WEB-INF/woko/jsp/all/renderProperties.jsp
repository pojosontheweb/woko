<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.facets.builtin.RenderProperties" %>
<%@ page import="woko.facets.builtin.RenderPropertyName" %>
<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.util.Util" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
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
                if (Util.hasProperty(owningObject, pName)) {

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
                } else {
                    Woko.logger.warn("renderProperties facet " + renderProperties +
                      " returned a non existing property : " +
                      owningObject.getClass().getName() + "." + pName);
                }
            }
        %>
        </tbody>
    </table>
</div>