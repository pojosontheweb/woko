<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="woko2.Woko" %>
<%@ tag import="woko2.util.Util" %>
<%@ tag import="java.util.Map" %>
<%@ attribute name="object" required="false" type="java.lang.Object" %>
<%@ attribute name="facetName" required="false" type="java.lang.String" %>
<%@ attribute name="facetContext" required="false" type="java.util.Map" %>
<%
    Woko woko = Woko.getWoko(application);
    Map ctx = facetContext;
    if (ctx==null) {
        String username = woko.getUsername(request);
        ctx = woko.createFacetContext(facetName, object, username);
    }
    Util.getFacetExecAndBind(woko, request, ctx);
%>