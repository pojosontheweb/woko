<%@ tag import="woko.Woko" %>
<%@ tag import="woko.util.LinkUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="object" required="false" type="java.lang.Object" %>
<%@ attribute name="targetObjectClass" required="false" type="java.lang.Class" %>
<%@ attribute name="facetName" required="false" type="java.lang.String" %>
<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="var" alias="daLink" scope="AT_END" %>
<%
    String url;
    if (object!=null) {
        url = LinkUtil.getUrl(Woko.getWoko(application), object, facetName);
    } else if (targetObjectClass!=null) {
        url = facetName + "/" + Woko.getWoko(application).getObjectStore().getClassMapping(targetObjectClass);
    } else {
        throw new IllegalStateException("neither object nor targetObjectClass provided !");
    }
    String path = request.getContextPath();
%>
<c:set var="daLink"><%=path + "/" + url%></c:set>
