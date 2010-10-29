<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<w:facet targetObject="${actionBean.object}" facetName="renderTitle"/>
<html>
  <head><title>Woko - ${renderTitle.title}</title></head>
  <body>
    <h1>Please confirm deletion</h1>
    <p>
        You are about to permanently delete object ${renderTitle.title}. Are you sure ?
        <w:objectClassName var="className" object="${actionBean.object}"/>
        <w:objectKey var="key" object="${actionBean.object}"/>
    </p>
    <form action="${pageContext.request.contextPath}/delete/${className}/${key}" method="POST">
        <input type="submit" name="facet.confirm" value="Delete"/>
        <input type="submit" name="facet.cancel" value="Cancel"/>
    </form>
  </body>
</html>