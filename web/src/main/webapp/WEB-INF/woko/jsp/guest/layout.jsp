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
            <script type="text/javascript" src="${pageContext.request.contextPath}/woko/js/woko.js"></script>
        </head>
        <body>
            <div id="wrap">
                <div id="header-space">
                    <div id="logo">
                        <img src="${pageContext.request.contextPath}/woko/woko-logo-small.png" alt="logo">
                    </div>
                    <div id="tagline">
                        You are not authenticated -
                        <a href="${pageContext.request.contextPath}/login">log-in</a>
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