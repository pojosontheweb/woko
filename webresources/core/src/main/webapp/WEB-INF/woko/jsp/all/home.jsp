<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="<%=WokoFacets.layout%>" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="renderTitle"/>

<fmt:message var="pageTitle" key="woko.guest.home.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <h1><fmt:message key="woko.guest.home.title"/> </h1>
        <p>
            <fmt:message key="woko.guest.home.content"/>
        </p>
    </s:layout-component>
</s:layout-render>