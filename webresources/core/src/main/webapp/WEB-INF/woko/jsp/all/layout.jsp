<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:username var="username"/>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<s:layout-definition>
    <html>
        <head>

            <c:choose>
                <c:when test="${not empty pageTitle}">
                    <title>${layout.appTitle} - ${pageTitle}</title>
                </c:when>
                <c:otherwise>
                    <title>${layout.appTitle}</title>
                </c:otherwise>
            </c:choose>
            <c:forEach items="${layout.cssIncludes}" var="cssLink">
                <link rel="stylesheet" href="${cp}${cssLink}" type="text/css">
            </c:forEach>
            <c:forEach items="${layout.jsIncludes}" var="jsLink">
                <script type="text/javascript" src="${cp}${jsLink}"></script>
            </c:forEach>
            <s:layout-component name="customCss"/>
            <s:layout-component name="customJs"/>

        </head>

        <body class="${bodyClass}">
            <div class="wokoAppTitle">${layout.appTitle}</div>
            <hr/>

            <c:if test="${skipLoginLink==null}">
                <div class="wokoAuthInfo">
                    <c:choose>
                        <c:when test="${username != null}">
                            <fmt:message key="woko.layout.loggedAs"/> <strong>${username}</strong> -
                            <a href="${cp}/logout"><fmt:message key="woko.layout.logout"/> </a>
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="woko.layout.notLogged"/>
                            <a href="${cp}/login"><fmt:message key="woko.layout.login"/> </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <hr/>

            <div class="wokoSearchBox">
                <s:form action="/search">
                    <s:text name="facet.query"/>
                    <s:submit name="search"/>
                </s:form>
            </div>
            <hr/>

            <div class="wokoNavBar">
                <w:includeFacet facetName="<%=WokoFacets.navBar%>" targetObject="${layout.facetContext.targetObject}"/>
            </div>
            <hr/>

            <div class="wokoContent">
                <s:messages/>
                <s:errors/>
                <s:layout-component name="body"/>
            </div>
            <hr/>

            <div class="wokoFooter">
                <img src="${cp}/woko/woko-logo-small.png" alt="logo"/>Powered by <a href="https://github.com/vankeisb/woko2">Woko</a>
            </div>

        </body>
    </html>
</s:layout-definition>