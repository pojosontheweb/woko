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

        <div class="container">

            <h1 class="page-header">
                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.h1.text">
                    <fmt:param value="${p.username}"/>
                </fmt:message>
            </h1>

            <s:form action="/password" class="form-horizontal">
                <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>
                <fieldset>

                    <w:b3-form-group-css fieldName="facet.currentPassword" var="css1"/>
                    <div class="${css1}">
                        <s:label for="facet.currentPassword" class="control-label col-sm-3">
                            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.currentPassword"/>
                        </s:label>
                        <div class="col-sm-4">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-lock"> </i></span>
                                <s:password name="facet.currentPassword" class="form-control"/>
                            </div>
                            <s:errors field="facet.currentPassword"/>
                        </div>
                    </div>

                    <w:b3-form-group-css fieldName="facet.newPassword" var="css2"/>
                    <div class="${css2}">
                        <s:label for="facet.newPassword" class="control-label col-sm-3">
                            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.newPassword"/>
                        </s:label>
                        <div class="col-sm-4">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-lock"> </i></span>
                                <s:password name="facet.newPassword" class="form-control"/>
                            </div>
                            <s:errors field="facet.newPassword"/>
                        </div>
                    </div>

                    <w:b3-form-group-css fieldName="facet.newPasswordConfirm" var="css3"/>
                    <div class="${css3}">
                        <s:label for="facet.newPasswordConfirm" class="control-label col-sm-3">
                            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.newPasswordConfirm"/>
                        </s:label>
                        <div class="col-sm-4">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-lock"> </i></span>
                                <s:password name="facet.newPasswordConfirm" class="form-control"/>
                            </div>
                            <s:errors field="facet.newPasswordConfirm"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-3">
                            <s:submit name="changePassword" class="btn btn-primary"/>
                        </div>
                    </div>

                </fieldset>
            </s:form>

        </div>

    </s:layout-component>
</s:layout-render>

