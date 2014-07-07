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

        <div class="container">

            <h1 class="page-header">
                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.reset.h1.text"/>
            </h1>

            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.password.reset.para.text"/>

            <s:form action="/resetPassword" role="form">
                <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>

                <w:b3-form-group-css fieldName="facet.email" var="emailGroupCss"/>
                <div class="${emailGroupCss}">
                    <s:label for="facet.email">
                        <fmt:message bundle="${wokoBundle}" key="User.email"/>
                    </s:label>
                    <div class="input-group">
                        <span class="input-group-addon">@</span>
                        <s:text name="facet.email" class="form-control"/>
                        <s:errors field="facet.email"/>
                    </div>
                </div>

                <div class="form-actions">
                    <s:submit name="emailToken" class="btn btn-primary"/>
                </div>
            </s:form>

        </div>

    </s:layout-component>
</s:layout-render>