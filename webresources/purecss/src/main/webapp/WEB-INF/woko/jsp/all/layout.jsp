<%--
~ Copyright 2001-2013 Remi Vankeisbelck
~
~ Licensed under the Apache License, Version 2.0 (the "License");
~ you may not use this file except in compliance with the License.
~ You may obtain a copy of the License at
~
~       http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing, software
~ distributed under the License is distributed on an "AS IS" BASIS,
~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
~ See the License for the specific language governing permissions and
~ limitations under the License.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp" %>

<%@ page import="java.util.Locale" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<w:username var="username"/>
<c:set var="cp" value="${pageContext.request.contextPath}" scope="request"/>
<w:cacheToken paramName="cacheToken" tokenValue="cacheTokenValue"/>
<c:set var="cacheTokenParams" value="${cacheToken}=${cacheTokenValue}" scope="request"/>
<s:layout-definition>
    <!DOCTYPE html>
    <html>
    <head>
        <%-- Add the woko favicon --%>
        <link rel="shortcut icon" href="${cp}/favicon.ico?${cacheTokenParams}"/>
        <%-- Needed by bootstrap to be responsive --%>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="utf-8">
        <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
        <!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js?${cacheTokenParams}"></script>
        <![endif]-->

        <%-- Display the pageTitle set in jsp's if any --%>
        <c:choose>
            <c:when test="${not empty pageTitle}">
                <title>${layout.appTitle} - ${pageTitle}</title>
            </c:when>
            <c:otherwise>
                <title>${layout.appTitle}</title>
            </c:otherwise>
        </c:choose>

        <%--  Import stylesheet
                - CSS from layout facet
                - CustomCSS
        ========================================================== --%>
        <c:forEach items="${layout.cssIncludes}" var="cssLink">
            <link rel="stylesheet" href="${cp}${cssLink}" type="text/css">
        </c:forEach>
        <s:layout-component name="customCss"/>


        <%--  Import javascript
                - JQuery
                - Bootstrap
                - Bootstrap datepicker
                - Woko
                - JS from layout facet
                - CustomJS
        ========================================================== --%>
        <script type="text/javascript" src="${cp}/js/jQuery-V1.7.1/jquery.min.js?${cacheTokenParams}"></script>
        <script type="text/javascript" src="${cp}/js/bootstrap-v2.3.0/bootstrap.min.js?${cacheTokenParams}"></script>
        <script type="text/javascript"
                src="${cp}/js/bootstrap-datepicker/bootstrap-datepicker.min.js?${cacheTokenParams}"></script>
        <script type="text/javascript" src="${cp}/woko/js/woko.base.js?${cacheTokenParams}"></script>
        <script type="text/javascript" src="${cp}/woko/js/woko.jquery.js?${cacheTokenParams}"></script>
        <script type="text/javascript" src="${cp}/woko/js/woko.rpc.js?${cacheTokenParams}"></script>

        <%-- Set the locale to the datepicker --%>
        <% Locale l = request.getLocale(); %>
        <script type="text/javascript">
            $(document).ready(function () {
                $('input[rel="datepicker"]').datepicker({language: "<%=l%>"});
            });
        </script>

        <% if (l.toString().equals("fr")) { %>
        <script type="text/javascript"
                src="${cp}/js/bootstrap-datepicker/bootstrap-datepicker.fr.min.js?${cacheTokenParams}"></script>
        <% } %>

        <script type="text/javascript">
            window.wokoClient = new woko.rpc.Client("${cp}");
        </script>

        <c:forEach items="${layout.jsIncludes}" var="jsLink">
            <script type="text/javascript" src="${cp}${jsLink}?${cacheTokenParams}"></script>
        </c:forEach>
        <s:layout-component name="customJs"/>

    </head>

    <body>
    <div id="wrapper">
        <div class="content">
            <%-- Header : the static navBar --%>
            <div class="header pure-u-1">
                <div class="pure-menu pure-menu-open pure-menu-fixed pure-menu-horizontal">
                    <div class="pure-u-1-3">
                        <%-- The app name --%>
                        <a href="${cp}/home" class="pure-menu-heading">${layout.appTitle}</a>
                        <%-- First display the navBar facet --%>
                        <ul>
                            <w:includeFacet facetName="<%=WokoFacets.navBar%>"
                                            targetObject="${layout.facetContext.targetObject}"/>
                        </ul>

                    </div>
                    <div class="pure-u-1-3">
                        <%-- Display user/connexion info --%>
                        <p>
                            <c:if test="${skipLoginLink==null}">
                                <c:choose>
                                    <c:when test="${username != null}">
                                        <strong>${username}</strong> -
                                        <a href="${cp}/logout"><fmt:message bundle="${wokoBundle}"
                                                                            key="woko.layout.logout"/> </a>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:message bundle="${wokoBundle}" key="woko.layout.notLogged"/>
                                        <a href="${cp}/login"><fmt:message bundle="${wokoBundle}"
                                                                           key="woko.layout.login"/>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </p>
                    </div>

                    <div class="pure-u-1-3">
                        <%-- Display the search input only for connected user --%>
                        <c:if test="${not empty username}">
                            <s:form action="/search"
                                    style="margin-right: 8px; height: 40px;">
                                <fmt:message bundle="${wokoBundle}" key="search" var="ph"/>
                                <s:text name="facet.query" class="search-query input-medium" placeholder="${ph}"/>
                            </s:form>
                        </c:if>
                    </div>
                </div>
            </div>


            <%-- Main content --%>

            <div class="content pure-u-1">
                <div class="pure-g-r">
                    <s:messages/>
                </div>
                <div class="pure-g-r">
                    <s:errors/>
                </div>
                <s:layout-component name="body"/>
            </div>


            <%-- Needed by the Sticky footer--%>
            <div id="push"></div>

        </div>

        <footer>
            <div class="content">
                <div class="footer pure-u-1">
                    Powered by
                    <a href="http://www.pojosontheweb.com">
                        <img src="${cp}/woko/woko-logo-small.png?${cacheTokenParams}" alt="logo" height="24px"/>
                    </a>
                </div>
            </div>
        </footer>
    </div>
    </body>
    </html>
</s:layout-definition>