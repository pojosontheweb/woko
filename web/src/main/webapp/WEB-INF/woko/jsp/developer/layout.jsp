<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
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
                                        Logged in as <strong>${username}</strong> -
                                        <a href="${pageContext.request.contextPath}/logout">logout</a>
                                    </c:when>
                                    <c:otherwise>
                                        You are not authenticated -
                                        <a href="${pageContext.request.contextPath}/login">log-in</a>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </c:if>
                    </div>
                    <div id="wokoSearchBox">
                        <s:form action="/search">
                            <s:text name="facet.query"/>
                            <s:submit name="search" value="Search"/>
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
                          <h1>Actions</h1>
                          <s:layout-component name="sidebarLinks"/>
                          <div class="poweredBy">
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