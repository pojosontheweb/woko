<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="woko.Woko" %>
<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="var" alias="daUser" scope="AT_END" %>
<c:set var="daUser" value="<%=Woko.getWoko(application).getUsername(request)%>"/>