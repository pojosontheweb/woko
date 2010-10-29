<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="layout" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="renderTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${renderTitle.title}">
    <s:layout-component name="body">
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
    </s:layout-component>
</s:layout-render>
