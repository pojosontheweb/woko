<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.login.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" skipLoginLink="true">
    <s:layout-component name="body">
        <h1><fmt:message bundle="${wokoBundle}" key="woko.login.title"/></h1>
          <form method="POST" action="j_security_check">
              <div>
                  <table>
                      <tbody>
                          <tr>
                              <td align="right"><fmt:message bundle="${wokoBundle}" key="user.username"/> </td>
                              <td><input type="text" name="j_username"/></td>
                          </tr>
                          <tr>
                              <td align="right"><fmt:message bundle="${wokoBundle}" key="user.password"/></td>
                              <td><input type="password" name="j_password"/></td>
                          </tr>
                          <tr>
                              <fmt:message bundle="${wokoBundle}" var="loginLabel" key="login"/>
                              <td colspan="2"><input type="submit" value="${loginLabel}" name="login"/></td>
                          </tr>
                      </tbody>
                  </table>
              </div>
          </form>
    </s:layout-component>
</s:layout-render>