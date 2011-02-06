<%@ page import="woko.actions.auth.builtin.WokoLogin" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<w:facet facetName="layout"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="Please log-in" skipLoginLink="true">
    <s:layout-component name="body">
        <h1>Please log-in</h1>
            <s:form beanclass="<%=WokoLogin.class%>">
                <s:hidden name="targetUrl"/>
                    <div>
                      <table>
                          <tbody>
                              <tr>
                                  <td align="right">Username</td>
                                  <td><s:text name="username"/></td>
                              </tr>
                              <tr>
                                  <td align="right">Password</td>
                                  <td><s:password name="password"/></td>
                              </tr>
                              <tr>
                                  <td colspan="2"><s:submit value="Login" name="login"/></td>
                              </tr>
                          </tbody>
                      </table>
                    </div>
          </s:form>
    </s:layout-component>
</s:layout-render>