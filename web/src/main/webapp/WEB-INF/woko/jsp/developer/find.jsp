<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="layout"/>

<fmt:message var="pageTitle" key="woko.devel.find.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="sidebarLinks">
        <ul class="menu">
            <li><a href="#"><fmt:message key="woko.actions.help"/> </a></li>
        </ul>
    </s:layout-component>
    <s:layout-component name="body">
        <h1><fmt:message key="woko.devel.find.fullText"/> </h1>
        <s:form action="/search">
            <fmt:message key="woko.devel.find.enterQuery"/>
            <s:text name="facet.query"/>
            <s:submit name="search"/>
        </s:form>
        <h1><fmt:message key="woko.devel.find.byClass"/> </h1>
        <p>
            <fmt:message key="woko.devel.find.selectName"/>
        </p>
        <ul>
            <c:forEach items="${find.mappedClasses}" var="className">
                <li><a href="${pageContext.request.contextPath}/list/${className}">${className}</a></li>    
            </c:forEach>
        </ul>
    </s:layout-component>
</s:layout-render>