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
<%@ page import="woko.actions.auth.builtin.WokoLogin" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.login.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">
        <h1><fmt:message bundle="${wokoBundle}" key="woko.login.title"/></h1>
            <s:form beanclass="<%=WokoLogin.class%>">
                <s:hidden name="targetUrl"/>
                    <div>
                      <table>
                          <tbody>
                              <tr>
                                  <td align="right"><s:label for="user.username"/> </td>
                                  <td><s:text name="username"/></td>
                              </tr>
                              <tr>
                                  <td align="right"><s:label for="user.password"/> </td>
                                  <td><s:password name="password"/></td>
                              </tr>
                              <tr>
                                  <td colspan="2"><s:submit name="login"/></td>
                              </tr>
                          </tbody>
                      </table>
                    </div>
          </s:form>
    </s:layout-component>
</s:layout-render>