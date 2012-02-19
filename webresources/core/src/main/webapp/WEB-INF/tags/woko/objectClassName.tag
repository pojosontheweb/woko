<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="woko.Woko" %>
<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="var" alias="daClass" scope="AT_END" %>
<%@ attribute name="object" required="true" type="java.lang.Object" %>
<c:set var="daClass"><%=Woko.getWoko(application).getObjectStore().getClassMapping(object.getClass())%></c:set>