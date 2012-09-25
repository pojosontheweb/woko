<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.builtin.*" %>
<%@ page import="net.sourceforge.stripes.util.CryptoUtil" %>
<%@ page import="woko.ext.usermanagement.facets.password.Password" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>
<%
    Password password = (Password)request.getAttribute(Password.FACET_NAME);
    String jspPath = password.getJspPath();
    String encryptedSourcePage = CryptoUtil.encrypt(jspPath);
%>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.password.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.h1.text">
                <fmt:param value="${p.username}"/>
            </fmt:message>
        </h1>

        <div class="row-fluid">
            <div class="span12">
                <s:form action="/password" class="form-horizontal">
                    <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>
                    <fieldset>

                        <div class="control-group ${empty(actionBean.context.validationErrors['facet.currentPassword']) ? '' : 'error'} ">
                            <s:label for="facet.currentPassword" class="control-label">
                                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.currentPassword"/>
                            </s:label>
                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on"><i class="icon-lock"> </i></span>
                                    <s:password name="facet.currentPassword" class="span4"/>
                                </div>
                                <s:errors field="facet.currentPassword"/>
                            </div>
                        </div>

                        <div class="control-group ${empty(actionBean.context.validationErrors['facet.newPassword']) ? '' : 'error'} ">
                            <s:label for="facet.newPassword" class="control-label">
                                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.newPassword"/>
                            </s:label>
                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on"><i class="icon-lock"> </i></span>
                                    <s:password name="facet.newPassword" class="span4"/>
                                </div>
                                <s:errors field="facet.newPassword"/>
                            </div>
                        </div>

                        <div class="control-group ${empty(actionBean.context.validationErrors['facet.newPasswordConfirm']) ? '' : 'error'} ">
                            <s:label for="facet.newPasswordConfirm" class="control-label">
                                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.newPasswordConfirm"/>
                            </s:label>
                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on"><i class="icon-lock"> </i></span>
                                    <s:password name="facet.newPasswordConfirm" class="span4"/>
                                </div>
                                <s:errors field="facet.newPasswordConfirm"/>
                            </div>
                        </div>

                        <div class="form-actions">
                            <s:submit name="changePassword" class="btn btn-primary btn-large"/>
                        </div>

                    </fieldset>
                </s:form>
            </div>
        </div>

    </s:layout-component>
</s:layout-render>

