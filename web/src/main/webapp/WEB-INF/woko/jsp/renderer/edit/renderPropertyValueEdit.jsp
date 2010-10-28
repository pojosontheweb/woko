<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Map renderPropertyValueResult = (Map)request.getAttribute("renderPropertyValueEditResult");
    Object propertyValue = renderPropertyValueResult.get("propertyValue");
    Class clazz = propertyValue.getClass();
    String className = clazz.getName();
%>
<span class="wokoPropertyValueEdit">
    <span class="<%=className%>">Unable to edit : <c:out value="<%=propertyValue.toString()%>"/></span>
</span>


