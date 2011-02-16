<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="layout" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="renderTitle"/>

<fmt:message var="pageTitle" key="guest.home.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="sidebarLinks">
        <ul class="menu">
            <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
            <li><a href="http://sourceforge.net/projects/woko">Woko</a></li>
        </ul>
    </s:layout-component>
    <s:layout-component name="body">
        <h1>Guest Home</h1>
        <p>
            This is guest home !
        </p>
    </s:layout-component>
</s:layout-render>