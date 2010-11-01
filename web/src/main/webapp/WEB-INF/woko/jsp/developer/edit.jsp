<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="layout" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="renderTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${renderTitle.title}">
    <s:layout-component name="sidebarLinks">
        <w:includeFacet facetName="renderLinksEdit" targetObject="${o}"/>
    </s:layout-component>
    <s:layout-component name="body">
        <w:includeFacet facetName="renderObjectEdit" targetObject="${o}"/>
    </s:layout-component>
</s:layout-render>