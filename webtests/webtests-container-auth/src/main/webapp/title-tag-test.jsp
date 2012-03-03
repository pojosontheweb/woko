<%@ include file="WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="test.MyEntity" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Simple jsp page</title></head>
<body>
    <%
        MyEntity e = new MyEntity();
        e.setId(1L);
        e.setProp1("foo");
    %>
<p>
    The title : <w:title object="<%=e%>"/>
</p>
</body>
</html>