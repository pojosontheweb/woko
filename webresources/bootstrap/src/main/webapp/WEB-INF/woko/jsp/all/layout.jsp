<%@ page import="java.util.Locale" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:username var="username"/>
<c:set var="cp" value="${pageContext.request.contextPath}"/>
<s:layout-definition>
    <!DOCTYPE html>
    <html>
    <head>

        <link rel="shortcut icon"
             href="${cp}/favicon.ico" />

        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="utf-8">

        <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

        <c:choose>
            <c:when test="${not empty pageTitle}">
                <title>${layout.appTitle} - ${pageTitle}</title>
            </c:when>
            <c:otherwise>
                <title>${layout.appTitle}</title>
            </c:otherwise>
        </c:choose>

        <script type="text/javascript" src="${cp}/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="${cp}/plugins/jquery-ui/js/jquery-ui-1.8.17.custom.min.js"></script>
        <%
            Locale l = request.getLocale();
        %>
        <script type="text/javascript">
            $(document).ready(function() {
                $.datepicker.setDefaults($.datepicker.regional['<%=l%>']);
            });
        </script>
        <%
            if (l.toString().equals("fr")) {
        %>
            <script type="text/javascript" src="${cp}/plugins/jquery-ui/js/ui.datepicker-fr.js"></script>
        <%
            }
        %>
        <script type="text/javascript" src="${cp}/bootstrap/js/bootstrap.min.js"></script>

        <c:forEach items="${layout.cssIncludes}" var="cssLink">
            <link rel="stylesheet" href="${cp}${cssLink}" type="text/css">
        </c:forEach>
        <c:forEach items="${layout.jsIncludes}" var="jsLink">
            <script type="text/javascript" src="${cp}${jsLink}"></script>
        </c:forEach>
        <s:layout-component name="customCss"/>
        <s:layout-component name="customJs"/>

        <link rel="stylesheet" type="text/css" href="${cp}/bootstrap/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="${cp}/plugins/jquery-ui/css/ui-lightness/jquery-ui-1.8.17.custom.css">

        <style type="text/css">
            .main-content {
                padding-top: 60px;
                padding-bottom: 40px;
            }

            .sidebar-nav {
                padding: 9px 0;
            }
        </style>
        <link rel="stylesheet" type="text/css" href="${cp}/bootstrap/css/bootstrap-responsive.min.css">
        <link rel="stylesheet" type="text/css" href="${cp}/bootstrap/css/bootstrap-woko.css">

    </head>

    <body>
    <div class="wrapper">
        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container-fluid">
                    <a data-target=".nav-collapse" data-toggle="collapse" class="btn btn-navbar">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a href="${cp}" class="brand">${layout.appTitle}</a>

                    <div class="nav-collapse">

                        <ul class="nav nav-pills">
                            <w:includeFacet facetName="<%=WokoFacets.navBar%>" targetObject="${layout.facetContext.targetObject}"/>
                        </ul>

                        <p class="navbar-text pull-right">
                            <c:if test="${skipLoginLink==null}">
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
                            </c:if>
                        </p>
                    </div>

                    <c:if test="${not empty username}">
                        <s:form action="/search" class="navbar-search">
                            <fmt:message key="search" var="ph"/>
                            <s:text name="facet.query" class="search-query" placeholder="${ph}"/>
                        </s:form>
                    </c:if>

                </div>
            </div>
        </div>

        <div class="main-content">
            <div class="container-fluid">
                <div class="row-fluid">
                    <s:messages/>
                    <s:errors/>
                    <s:layout-component name="body"/>
                </div>
                <div class="push"></div>
            </div>
        </div>
    </div>

    <footer>
        <div class="container-fluid">
            <div class="pull-right">
                Powered by <a href="https://github.com/vankeisb/woko2"><img src="${cp}/woko/woko-logo-small.png"
                                                                            alt="logo" height="24px"/></a>
            </div>
        </div>
    </footer>

    </body>
    </html>
</s:layout-definition>