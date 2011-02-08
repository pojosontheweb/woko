<%@ tag import="woko.Woko" %>
<%@ tag import="woko.util.LinkUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="object" required="true" type="java.lang.Object" %>
<%@ attribute name="facetName" required="false" type="java.lang.String" %>
<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="var" alias="daKey" scope="AT_END" %>
<%
    String url = LinkUtil.getUrl(Woko.getWoko(application), object, facetName);
    String path = request.getContextPath();
%>
<c:set var="daLink"><%=path + "/" + url%></c:set>
