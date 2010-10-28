<%@ tag import="java.util.Map" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="object" required="false" type="java.lang.Object" %>
<%@ attribute name="facetName" required="false" type="java.lang.String" %>
<%@ attribute name="facetContext" required="false" type="java.util.Map" %>
<w:facet object="${object}" facetName="${facetName}" facetContext="${facetContext}"/>
<%
    String fName = facetName;
    if (fName==null) {
        fName = (String)((Map)facetContext).get("facetName");
    }
    Map result = (Map)request.getAttribute(fName + "Result");
%>
<jsp:include page="<%=(String)result.get("fragmentPath")%>"/>