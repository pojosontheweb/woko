<%@ page import="java.util.Map" %>
<%@ page import="java.util.Stack" %>
<%@ page import="woko2.util.Util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    Map rpver = (Map)request.getAttribute("renderPropertyValueEditResult");
    String propertyName = (String)rpver.get("propertyName");
    Object propVal = rpver.get("propertyValue");
    String className = propVal.getClass().getSimpleName();
    Stack<String> propChain = (Stack<String>)request.getAttribute("propChain");
    if (propChain!=null) {
        propertyName = Util.computePropertyPath(propChain) + "." + propertyName;
    }
%>
<span class="wokoPropertyValueEdit">
    <span class="<%=className%>">
        <s:text name="<%="object." + propertyName%>"/>
    </span>
</span>