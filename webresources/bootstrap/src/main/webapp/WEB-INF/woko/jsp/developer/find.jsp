<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message var="pageTitle" key="woko.devel.find.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <h1 class="page-header"><fmt:message key="woko.devel.find.pageTitle"/></h1>
        <h2><fmt:message key="woko.devel.find.fullText"/> </h2>
        <s:form action="/search" class="form-inline" method="GET">
            <fmt:message key="woko.devel.find.enterQuery"/>
            <s:text name="facet.query" class="input-xlarge"/>
            <s:submit name="search" class="btn btn-primary"/>
        </s:form>
        <h2><fmt:message key="woko.devel.find.byClass"/> </h2>
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