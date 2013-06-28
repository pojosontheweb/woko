<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.builtin.*" %>
<%@ page import="net.sourceforge.stripes.util.CryptoUtil" %>
<%@ page import="woko.ext.usermanagement.facets.password.ResetPassword" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>
<%
    ResetPassword password = (ResetPassword)request.getAttribute(ResetPassword.FACET_NAME);
    String jspPath = password.getJspPath();
    String encryptedSourcePage = CryptoUtil.encrypt(jspPath);
%>
<fmt:message bundle="${wokoBundle}"  var="pageTitle" key="woko.ext.usermanagement.set.newPassword.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.set.newPassword.title"/>
        </h1>

        <div class="row-fluid">
            <s:form action="/resetPassword" class="form-horizontal">
                <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>
                <s:hidden name="facet.email"/>
                <fieldset>

                    <div class="control-group ${empty(actionBean.context.validationErrors['facet.password1']) ? '' : 'error'} ">
                        <s:label for="facet.password1" class="control-label">
                            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.set.newPassword.password1"/>
                        </s:label>
                        <div class="controls">
                            <s:password name="facet.password1" class="input-xlarge"/>
                            <s:errors field="facet.password1"/>
                        </div>
                    </div>

                    <div class="control-group ${empty(actionBean.context.validationErrors['facet.password2']) ? '' : 'error'} ">
                        <s:label for="facet.password2" class="control-label">
                            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.set.newPassword.password2"/>
                        </s:label>
                        <div class="controls">
                            <s:password name="facet.password2" class="input-xlarge"/>
                            <s:errors field="facet.password2"/>
                        </div>
                    </div>

                    <div class="form-actions">
                        <s:submit name="doSetPassword" class="btn btn-primary"/>
                    </div>

                </fieldset>
            </s:form>
        </div>
    </s:layout-component>
</s:layout-render>

