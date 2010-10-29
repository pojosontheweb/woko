<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="o" value="${actionBean.object}"/>
<w:facet targetObject="${o}" facetName="renderTitle" var="rt"/>
<html>
  <head><title>Woko - ${rt.title}</title></head>
  <body>
    <w:includeFacet facetName="renderLinks" targetObject="${o}"/>
    <w:includeFacet facetName="renderObject" targetObject="${o}"/>
  </body>
</html>