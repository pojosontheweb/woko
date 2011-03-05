<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<html>
<head>

</head>
<body>
<h1>facet validation test</h1>
<s:messages/>
<s:errors/>
<s:form action="/testValidate">
  <s:text name="facet.prop"/>
  <s:submit name="doIt"/>
</s:form>
</body>
</html>
