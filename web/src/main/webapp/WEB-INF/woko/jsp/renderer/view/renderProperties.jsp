<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Collection" %>
<%@ page import="woko2.Woko" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Woko woko = Woko.getWoko(application);
    Map rpr = (Map)request.getAttribute("renderPropertiesResult");
    Collection propNames = (Collection)rpr.get("propertyNames");
    Map properties = (Map)rpr.get("properties");
    Object owningObject = rpr.get("owningObject");
    String username = (String)rpr.get("username");
%>
<div class="wokoProperties">
    <table border="1">
        <tbody>
        <%
            for (Object pName : propNames) {
                Object pVal = properties.get(pName);
                Map fc1 = woko.createFacetContext("renderPropertyName", pName, username);
                fc1.put("propertyName", pName);
                fc1.put("owningObject", owningObject);
                Map fc2 = woko.createFacetContext("renderPropertyValue", pVal, username);
                fc2.put("propertyName", pName);
                fc2.put("owningObject", owningObject);
        %>
        <tr>
            <th><w:includeFacet facetContext="<%=fc1%>"/></th>
            <td><w:includeFacet facetContext="<%=fc2%>"/></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>