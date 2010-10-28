<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Map renderPropertyValueResult = (Map)request.getAttribute("renderPropertyValueResult");
    Object propertyValue = renderPropertyValueResult.get("propertyValue");
    Class clazz = propertyValue.getClass();
    String className = clazz.getName();
%>
<span class="wokoPropertyValue">
    <span class="<%=className%>"><c:out value="<%=propertyValue.toString()%>"/></span>
</span>


