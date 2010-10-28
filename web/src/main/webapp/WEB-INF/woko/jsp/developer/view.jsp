<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<w:facet object="${actionBean.object}" facetName="renderTitle"/>
<html>
  <head><title>Woko - ${renderTitleResult.title}</title></head>
  <body>
    <w:includeFacet facetName="renderLinks" object="${actionBean.object}"/>
    <w:includeFacet facetName="renderObject" object="${actionBean.object}"/>
  </body>
</html>