<%@ page import="woko2.actions.auth.builtin.WokoLogin" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
</head>
<body>
<s:messages/>
<s:errors/>
<s:form beanclass="<%=WokoLogin.class%>">
    <s:hidden name="targetUrl"/>
    <table style="width: 100%;">
        <tbody>
        <tr>
            <td>
                username
            </td>
            <td>
                <s:text name="username"/>
            </td>
        </tr>
        <tr>
            <td>
                password
            </td>
            <td>
                <s:password name="password"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="right">
                <s:submit name="login" value="Log-in"/>
            </td>
        </tr>
        </tbody>
    </table>
</s:form>
</body>
</html>