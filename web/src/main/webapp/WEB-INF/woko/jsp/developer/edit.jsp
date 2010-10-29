<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="o" value="${actionBean.object}"/>
<w:facet targetObject="${o}" facetName="renderTitle"/>
<html>
  <head><title>Woko - ${renderTitle.title} (editing)</title></head>
  <body>
        TODO
  </body>
</html>