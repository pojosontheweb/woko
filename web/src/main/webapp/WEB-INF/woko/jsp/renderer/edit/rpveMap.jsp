<%@ page import="java.util.Map" %>
<%@ page import="java.util.Stack" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%
    Map rpr = (Map)request.getAttribute("renderPropertiesEditResult");

    Map rper = (Map)request.getAttribute("renderPropertyValueEditResult");
    String propertyName = (String)rper.get("propertyName");
    Object propertyValue = rper.get("propertyValue");
    Stack<String> propChain = (Stack<String>)request.getAttribute("propChain");
    if (propChain==null) {
        propChain = new Stack<String>();
        request.setAttribute("propChain", propChain);
    }
    propChain.push(propertyName);
%>
<span class="wokoPropertyValueEdit">
    <span class="Map">
        <w:includeFacet object="<%=propertyValue%>" facetName="renderPropertiesEdit"/>
    </span>
</span>
<%
    propChain.pop();
    request.setAttribute("renderPropertiesEditResult", rpr);
%>