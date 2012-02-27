<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:username var="username"/>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<s:layout-definition>
    <html>
        <head>
            <link rel="shortcut icon"
                 href="${cp}/favicon.ico" />
            <c:choose>
                <c:when test="${not empty pageTitle}">
                    <title>${layout.appTitle} - ${pageTitle}</title>
                </c:when>
                <c:otherwise>
                    <title>${layout.appTitle}</title>
                </c:otherwise>
            </c:choose>
            <link rel="stylesheet" href="${cp}/woko/css/layout-all.css" type="text/css">
            <link rel="stylesheet" href="${cp}/woko/css/lithium/assets/style.css" type="text/css">
            <c:forEach items="${layout.cssIncludes}" var="cssLink">
                <link rel="stylesheet" href="${pageContext.request.contextPath}${cssLink}" type="text/css">
            </c:forEach>
            <c:forEach items="${layout.jsIncludes}" var="jsLink">
                <script type="text/javascript" src="${pageContext.request.contextPath}${jsLink}"></script>
            </c:forEach>
            <script type="text/javascript" src="${pageContext.request.contextPath}/woko/js/woko.base.js"></script>
            <s:layout-component name="customCss"/>
            <s:layout-component name="customJs"/>
        </head>
        <body class="${bodyClass}">
            <div id="wrap">
                <div id="header-space">
                    <div id="logo">
                        <img src="${pageContext.request.contextPath}/woko/woko-logo-small.png" alt="logo">
                    </div>
                    <div id="tagline">
                        <c:if test="${skipLoginLink==null}">
                            <span class="authInfo">
                                <c:choose>
                                    <c:when test="${username != null}">
                                        <fmt:message bundle="${wokoBundle}" key="woko.layout.loggedAs"/> <strong>${username}</strong> -
                                        <a href="${pageContext.request.contextPath}/logout"><fmt:message bundle="${wokoBundle}" key="woko.layout.logout"/> </a>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message bundle="${wokoBundle}" key="woko.layout.notLogged"/>
                                        <a href="${pageContext.request.contextPath}/login"><fmt:message bundle="${wokoBundle}" key="woko.layout.login"/> </a>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </c:if>
                    </div>
                    <div id="wokoSearchBox">
                        <s:form action="/search">
                            <s:text name="facet.query"/>
                            <s:submit name="search"/>
                        </s:form>
                    </div>
                </div>
                <div id="navbar">
                    <div id="nbar">
                        <w:includeFacet facetName="<%=WokoFacets.navBar%>" targetObject="${layout.facetContext.targetObject}"/>
                    </div>
                </div>
                <div id="content-wrap">
                    <div id="content">
                        <s:messages/>
                        <s:errors/>
                        <s:layout-component name="body"/>
                    </div>
                </div>
            </div>
            <div class="clearfix"></div>
            <div id="divider-line"></div>
        </body>
    </html>
</s:layout-definition>