<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="<%=WokoFacets.layout%>" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${renderTitle.title}">
    <s:layout-component name="body">
        <w:includeFacet facetName="<%=WokoFacets.renderObjectEdit%>" targetObject="${o}"/>
    </s:layout-component>
</s:layout-render>