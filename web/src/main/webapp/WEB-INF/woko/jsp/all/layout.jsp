<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:username var="username"/>

<s:layout-definition>
    <html>
        <head>
            <title>${layout.appTitle} - ${pageTitle}</title>
            <c:forEach items="${layout.cssIncludes}" var="cssLink">
                <link rel="stylesheet" href="${pageContext.request.contextPath}${cssLink}" type="text/css">
            </c:forEach>
            <c:forEach items="${layout.jsIncludes}" var="jsLink">
                <script type="text/javascript" src="${pageContext.request.contextPath}${jsLink}"></script>
            </c:forEach>
            <script type="text/javascript" src="${pageContext.request.contextPath}/woko/js/woko.js"></script>
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
                                        <fmt:message key="woko.layout.loggedAs"/> <strong>${username}</strong> -
                                        <a href="${pageContext.request.contextPath}/logout"><fmt:message key="woko.layout.logout"/> </a>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message key="woko.layout.notLogged"/>
                                        <a href="${pageContext.request.contextPath}/login"><fmt:message key="woko.layout.login"/> </a>
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
                        <w:includeFacet facetName="navBar" targetObject="${layout.facetContext.targetObject}"/>
                    </div>
                </div>
                <div id="content-wrap">
                    <div id="sidebar">
                        <div id="left-sbar">
                        <div class="widgetspace">
                          <h1><fmt:message key="woko.layout.actions"/> </h1>
                          <s:layout-component name="sidebarLinks"/>
                          <div class="poweredBy">
                              <%-- Intentionally not localized (we always use 'Powered by Woko' ) --%>
                              Powered by <a href="#">Woko</a>
                          </div>
                        </div>
                      </div>
                    </div>
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