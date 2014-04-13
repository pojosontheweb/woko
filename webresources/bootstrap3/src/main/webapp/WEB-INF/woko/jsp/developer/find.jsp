<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.find.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <div class="container">

            <h1 class="page-header">
                <fmt:message bundle="${wokoBundle}" key="woko.devel.find.pageTitle"/>
            </h1>

            <s:form action="/search" class="form-inline" method="GET" role="search">
                <div class="row">
                    <div class="col-xs-8">
                        <div class="input-group">
                            <fmt:message bundle="${wokoBundle}" key="woko.devel.find.enterQuery" var="placeholder"/>
                            <s:text name="facet.query" class="form-control" placeholder="${placeholder}"/>
                            <span class="input-group-btn">
                                <button class="btn btn-primary" type="submit" value="search">
                                    <i class="glyphicon glyphicon-search"> </i>
                                </button>
                            </span>
                        </div>
                    </div>
                </div>
            </s:form>

        <h2>
                <fmt:message bundle="${wokoBundle}" key="woko.devel.find.byClass"/>
            </h2>

            <ul>
                <c:forEach items="${find.mappedClasses}" var="className">
                    <li><a href="${pageContext.request.contextPath}/list/${className}">${className}</a></li>
                </c:forEach>
            </ul>
        </div>
    </s:layout-component>
</s:layout-render>