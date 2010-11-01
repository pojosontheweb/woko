<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<w:facet facetName="layout"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="Please log in" skipLoginLink="true">
    <s:layout-component name="body">
        <h1>Please log-in</h1>
          <form method="POST" action="j_security_check">
              <div>
                  <table>
                      <tbody>
                          <tr>
                              <td align="right">Username</td>
                              <td><input type="text" name="j_username"/></td>
                          </tr>
                          <tr>
                              <td align="right">Password</td>
                              <td><input type="password" name="j_password"/></td>
                          </tr>
                          <tr>
                              <td colspan="2"><input type="submit" value="Login"/></td>
                          </tr>
                      </tbody>
                  </table>
              </div>
          </form>
    </s:layout-component>
</s:layout-render>