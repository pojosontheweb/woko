<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="woko2.Woko" %>
<%@ tag import="woko2.facets.FacetNotFoundException" %>
<%@ tag import="woko2.facets.FragmentFacet" %>
<%@ attribute name="targetObject" required="false" type="java.lang.Object" %>
<%@ attribute name="targetObjectClass" required="false" type="java.lang.Class" %>
<%@ attribute name="facetName" required="true" type="java.lang.String" %>
<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="var" alias="daFacet" scope="AT_END" %>
<%
    Woko woko = Woko.getWoko(application);
    // binds facet to request
    Object f = woko.getFacet(facetName, request, targetObject, targetObjectClass);
    if (f==null) {
        throw new FacetNotFoundException(facetName, targetObject, targetObjectClass, woko.getUsername(request));
    }
    if (!(f instanceof FragmentFacet)) {
        throw new IllegalStateException("Facet " + f + " doesn't implement FragmentFacet");
    }
%>
<c:set var="daFacet"><%=f%></c:set>