<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<html>
<head>
    <title>UTF-8 message test</title>
</head>
<body>
<h1>Woko.getLocalizedMessage()</h1>
<span id="msgWokoApi">
    <c:out value="${facet.wokoMessage}"/>
</span>

<h1>w:message</h1>
<span id="msgWokoTag">
    <w:message key="utf8.message.test"/>
</span>

<h1>w:message (with arg)</h1>
<span id="msgWokoTagWithArg">
    <w:message key="utf8.message.test.with.arg">
        <w:msg-param value="allôôô"/>
    </w:message>
</span>

</body>
</html>
