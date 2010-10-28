<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<w:facet object="${actionBean.object}" facetName="renderTitle"/>
<html>
  <head><title>Woko - ${renderTitleResult.title}</title></head>
  <body>
    <h1>Please confirm deletion</h1>
    <p>
        You are about to permanently delete object ${renderTitleResult.title}. Are you sure ?
        <w:objectClassName var="className" object="${actionBean.object}"/>
        <w:objectKey var="key" object="${actionBean.object}"/>
        <form action="${pageContext.request.contextPath}/delete/${className}/${key}" method="POST">
            <input type="submit" name="delete" value="Delete"/>
            <input type="submit" name="cancel" value="Cancel"/>
        </form>
    </p>
  </body>
</html>