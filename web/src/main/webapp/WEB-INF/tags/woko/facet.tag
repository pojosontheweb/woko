<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="woko2.Woko" %>
<%@ tag import="woko2.facets.FragmentFacet" %>
<%@ attribute name="targetObject" required="false" type="java.lang.Object" %>
<%@ attribute name="targetObjectClass" required="false" type="java.lang.Class" %>
<%@ attribute name="facetName" required="true" type="java.lang.String" %>
<%
    Woko woko = Woko.getWoko(application);
    // binds facet to request
    Object f = woko.getFacet(facetName, request, targetObject, targetObjectClass, true);
    if (!(f instanceof FragmentFacet)) {
        throw new IllegalStateException("Facet " + f + " doesn't implement FragmentFacet");
    }
%>