<%@ page import="woko.exceptions.handlers.WokoAutoExceptionHandler" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--
  ~ Copyright 2001-2010 Remi Vankeisbelck
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~       http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

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