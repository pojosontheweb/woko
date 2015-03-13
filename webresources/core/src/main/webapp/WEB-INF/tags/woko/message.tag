<%--
  ~ Copyright 2001-2012 Remi Vankeisbelck
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
<%@ tag import="woko.Woko" %>
<%@ tag import="woko.persistence.ObjectStore" %>
<%@ tag import="java.util.List" %>
<%@ tag import="java.util.ArrayList" %>
<%@ tag import="java.util.Locale" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="key" required="true" type="java.lang.String" %>
<%@ attribute name="locale" required="false" type="java.util.Locale" %>
<%@ attribute name="escapeXml" required="false" type="java.lang.Boolean" %>
<%@ attribute name="var" required="false" type="java.lang.String" rtexprvalue="false" %>
<%
    List msgParams = new ArrayList();
    request.setAttribute("__woko_msg_params", msgParams);
%>
<jsp:doBody/>
<%
    boolean escape = escapeXml == null || escapeXml.booleanValue();
    Woko<?,?,?,?> woko = Woko.getWoko(application);
    String[] args = new String[msgParams.size()];
    args = (String[])msgParams.toArray(args);
    String formattedMsg;
    if (locale!=null) {
        formattedMsg = woko.getLocalizedMessage(locale, key, args);
    } else {
        formattedMsg = woko.getLocalizedMessage(request, key, args);
    }
    if (var!=null) {
        request.setAttribute(var, formattedMsg);
    } else {
%>
<c:out value="<%=formattedMsg%>" escapeXml="<%=escape%>"/>
<%
    }
%>
