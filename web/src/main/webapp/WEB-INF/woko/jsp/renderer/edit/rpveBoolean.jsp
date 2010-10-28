<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    Map rpver = (Map)request.getAttribute("renderPropertyValueEditResult");
    String propertyName = (String)rpver.get("propertyName");
    Boolean propVal = (Boolean)rpver.get("propertyValue");
%>
<span class="wokoPropertyValueEdit">
    <span class="Boolean">
        <s:select name="<%="object." + propertyName%>">
            <s:option value="true" selected="<%=propVal ? "true" : "false"%>">true</s:option>
            <s:option value="false" selected="<%=!propVal ? "true" : "false"%>">false</s:option>            
        </s:select>
    </span>
</span>