<%--
  ~ Copyright 2001-2010 Remi Vankeisbelck
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
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
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
                            <fmt:message bundle="${wokoBundle}" key="woko.layout.loggedAs"/> <strong>${username}</strong> -
                            <a href="${cp}/logout"><fmt:message bundle="${wokoBundle}" key="woko.layout.logout"/> </a>
                        </c:when>
                        <c:otherwise>
                            <fmt:message bundle="${wokoBundle}" key="woko.layout.notLogged"/>
                            <a href="${cp}/login"><fmt:message bundle="${wokoBundle}" key="woko.layout.login"/> </a>
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