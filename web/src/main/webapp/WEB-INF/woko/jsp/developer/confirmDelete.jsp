<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="layout" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="renderTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${renderTitle.title}">
    <s:layout-component name="body">
        <h1><fmt:message key="woko.devel.confirmDelete.title"/></h1>
        <p>
            <fmt:message key="woko.devel.confirmDelete.question">
                <fmt:param value="${renderTitle.title}"/>
            </fmt:message>
            
            <w:objectClassName var="className" object="${actionBean.object}"/>
            <w:objectKey var="key" object="${actionBean.object}"/>
        </p>
        <%--<form action="${pageContext.request.contextPath}/delete/${className}/${key}" method="POST">--%>
            <%--<input type="submit" name="facet.confirm" value="Delete"/>--%>
            <%--<input type="submit" name="facet.cancel" value="Cancel"/>--%>
        <%--</form>--%>

        <s:form action="${pageContext.request.contextPath}/delete/${className}/${key}">
            <s:submit name="facet.confirm"/>
            <s:submit name="facet.cancel"/>
        </s:form>

    </s:layout-component>
</s:layout-render>
