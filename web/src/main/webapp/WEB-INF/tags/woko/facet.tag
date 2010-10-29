<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="woko2.Woko" %>
<%@ attribute name="targetObject" required="false" type="java.lang.Object" %>
<%@ attribute name="targetObjectClass" required="false" type="java.lang.Class" %>
<%@ attribute name="facetName" required="true" type="java.lang.String" %>
<%
    Woko woko = Woko.getWoko(application);
    // binds facet to request, throws if not found
    woko.getFacet(facetName, request, targetObject, targetObjectClass, true);
%>