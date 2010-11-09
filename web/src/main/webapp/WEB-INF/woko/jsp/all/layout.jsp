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
            <s:layout-component name="banner">
                <div id="banner">
                    <h1>${layout.appTitle}</h1>
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
            </s:layout-component>
            <div class="messages">
                <s:errors/>
                <s:messages/>
            </div>
            <div id="navigation">
                <w:includeFacet facetName="navBar" targetObject="${layout.facetContext.targetObject}"/>
            </div>
            <div id="content">
                <s:layout-component name="sidebarLinks"/>
                <s:layout-component name="body"/>
            </div>
        </body>
    </html>
</s:layout-definition>