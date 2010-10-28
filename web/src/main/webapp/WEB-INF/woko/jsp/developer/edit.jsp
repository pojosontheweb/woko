<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<w:facet object="${actionBean.object}" facetName="renderTitle"/>
<html>
  <head><title>Woko - editing ${renderTitleResult.title}</title></head>
  <body>
    <w:includeFacet facetName="renderLinksEdit" object="${actionBean.object}"/>
    <w:includeFacet facetName="renderObjectEdit" object="${actionBean.object}"/>
  </body>
</html>