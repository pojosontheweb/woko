<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="layout"/>
<%-- Use standard jstl to localize form --%>
<fmt:setBundle basename="StripesResources"/>

<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="Please log-in" skipLoginLink="true">
    <s:layout-component name="body">
        <h1>Please log-in</h1>
          <form method="POST" action="j_security_check">
              <div>
                  <table>
                      <tbody>
                          <tr>
                              <td align="right"><fmt:message key="user.username"/> </td>
                              <td><input type="text" name="j_username"/></td>
                          </tr>
                          <tr>
                              <td align="right"><fmt:message key="user.password"/></td>
                              <td><input type="password" name="j_password"/></td>
                          </tr>
                          <tr>
                              <fmt:message var="loginLabel" key="login"/>
                              <td colspan="2"><input type="submit" value="${loginLabel}" name="login"/></td>
                          </tr>
                      </tbody>
                  </table>
              </div>
          </form>
    </s:layout-component>
</s:layout-render>