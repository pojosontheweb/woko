<%@ page import="java.util.Map" %>
<%@ page import="java.util.Collection" %>
<%@ page import="woko2.facets.Facet" %>
<%@ page import="woko2.Woko" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Woko woko = Woko.getWoko(application);
    Map rpr = (Map)request.getAttribute("renderPropertiesEditResult");
    Collection propNames = (Collection)rpr.get("propertyNames");
    Map properties = (Map)rpr.get("properties");
    Object owningObject = rpr.get("owningObject");
    String username = woko.getUsername(request);
%>
<div class="wokoProperties">
    <table border="1">
        <tbody>
        <%
            for (Object pName : propNames) {
                Map fc1 = woko.createFacetContext("renderPropertyName", pName, username);
                fc1.put("propertyName", pName);
                fc1.put("owningObject", owningObject);

                Object pVal = properties.get(pName);
                Map fc2 = woko.createFacetContext("renderPropertyValueEdit", pVal, username);
                fc2.put("propertyName", pName);
                fc2.put("owningObject", owningObject);
                Facet facet = woko.getFacet(fc2);
                if (facet==null) {
                    // nothing to edit this property, just display it
                    fc2.put("facetName", "renderPropertyValue");
                    facet = woko.getFacet(fc2);
                    if (facet==null) {
                        throw new IllegalStateException("No renderPropertyValue[Edit] found for context " + fc2);
                    }
                }
        %>
        <tr>
            <th><w:includeFacet facetContext="<%=fc1%>"/></th>
            <td><w:includeFacet facetContext="<%=fc2%>"/></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>