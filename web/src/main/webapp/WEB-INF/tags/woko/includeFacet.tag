<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="targetObject" required="false" type="java.lang.Object" %>
<%@ attribute name="targetObjectClass" required="false" type="java.lang.Class" %>
<%@ attribute name="facetName" required="false" type="java.lang.String" %>
<w:facet targetObject="${targetObject}"
         targetObjectClass="${targetObjectClass}"
         facetName="${facetName}"
         var="theFacet"/>
<jsp:include page="${theFacet.fragmentPath}"/>