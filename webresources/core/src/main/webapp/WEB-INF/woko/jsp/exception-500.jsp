<%@ page import="woko.exceptions.handlers.WokoAutoExceptionHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Error</title></head>
<body>
<h1>An error occured</h1>
<p>
    We're sorry, an error occured. Please try refreshing the page, or try again later !
</p>
<p>
    Error ticket : <i><%=WokoAutoExceptionHandler.getTicket(request)%></i>
</p>
</body>
</html>