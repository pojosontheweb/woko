<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag import="woko.Woko" %>
<%@ tag import="woko.persistence.ObjectStore" %>
<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="var" alias="daClass" scope="AT_END" %>
<%@ attribute name="clazz" required="true" type="java.lang.Class" %>
<%
    Woko woko = Woko.getWoko(application);
    ObjectStore os = woko.getObjectStore();
    String classMapping = os.getClassMapping(clazz);
%>
<c:set var="daClass"><%=classMapping%></c:set>