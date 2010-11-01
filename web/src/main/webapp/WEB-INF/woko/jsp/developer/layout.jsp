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
        </head>
        <body>
            <div id="wrap">
                <div id="header-space">
                    <div id="logo">
                        <img src="${pageContext.request.contextPath}/woko/css/lithium/assets/logo.png" alt="logo">
                    </div>
                    <div id="company-name">Woko</div>
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
                </div>
                <div id="navbar">
                    <div id="nbar">
                        <w:includeFacet facetName="navBar" targetObject="${layout.context.targetObject}"/>
                    </div>
                </div>
                <div id="content-wrap">
                    <div id="sidebar">
                        <div id="left-sbar">
                        <div class="widgetspace">
                          <h1>Sidebar Links</h1>
                          <s:layout-component name="sidebarLinks"/>
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
            <div id="footer"> &copy; 2010 <strong>Woko</strong> | Web Design by: <a href="http://www.themebin.com/">ThemeBin</a> </div>                    
        </body>
    </html>
</s:layout-definition>