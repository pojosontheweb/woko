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
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.password.reset.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.reset.h1.text">
                <fmt:param value="${p.username}"/>
            </fmt:message>
        </h1>

        <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.reset.para.text"/>

        <div class="row-fluid">
            <div class="span12">
                <s:form action="/resetPassword" class="form-horizontal">
                    <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>
                    <fieldset>

                        <div class="control-group ${empty(actionBean.context.validationErrors['facet.email']) ? '' : 'error'} ">
                            <s:label for="facet.email" class="control-label">
                                <fmt:message bundle="${wokoBundle}" key="User.email"/>
                            </s:label>
                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on">@</span>
                                    <s:text name="facet.email" class="span4"/>
                                </div>
                                <s:errors field="facet.email"/>
                            </div>
                        </div>

                        <div class="form-actions">
                            <s:submit name="emailToken" class="btn btn-primary btn-large"/>
                        </div>

                    </fieldset>
                </s:form>
            </div>
        </div>

    </s:layout-component>
</s:layout-render>

