<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.builtin.*" %>
<%@ page import="net.sourceforge.stripes.util.CryptoUtil" %>
<%@ page import="woko.ext.usermanagement.facets.registration.Register" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>
<%
    Register register = (Register)request.getAttribute(Register.FACET_NAME);
    String jspPath = register.getJspPath();
    String encryptedSourcePage = CryptoUtil.encrypt(jspPath);
%>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.register.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.h1.text">
                <fmt:param value="${postRegister.username}"/>
            </fmt:message>
        </h1>

        <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.para.text"/>

        <s:form action="/register" class="form-horizontal">
            <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>
            <fieldset>

                <div class="control-group ${empty(actionBean.context.validationErrors['facet.username']) ? '' : 'error'} ">
                    <s:label for="facet.username" class="control-label">
                        <fmt:message bundle="${wokoBundle}" key="User.username"/>
                    </s:label>
                    <div class="controls">
                        <s:text name="facet.username"/>
                        <s:errors field="facet.username"/>
                    </div>
                </div>

                <div class="control-group ${empty(actionBean.context.validationErrors['facet.email']) ? '' : 'error'} ">
                    <s:label for="facet.email" class="control-label">
                        <fmt:message bundle="${wokoBundle}" key="User.email"/>
                    </s:label>
                    <div class="controls">
                        <s:text name="facet.email"/>
                        <s:errors field="facet.email"/>
                    </div>
                </div>

                <div class="control-group ${empty(actionBean.context.validationErrors['facet.password1']) ? '' : 'error'} ">
                    <s:label for="facet.password1" class="control-label">
                        <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password1"/>
                    </s:label>
                    <div class="controls">
                        <s:password name="facet.password1"/>
                        <s:errors field="facet.password1"/>
                    </div>
                </div>

                <div class="control-group ${empty(actionBean.context.validationErrors['facet.password2']) ? '' : 'error'} ">
                    <s:label for="facet.password2" class="control-label">
                        <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password2"/>
                    </s:label>
                    <div class="controls">
                        <s:password name="facet.password2"/>
                        <s:errors field="facet.password2"/>
                    </div>
                </div>


                <div class="form-actions">
                    <s:submit name="doRegister" class="btn btn-primary btn-large"/>
                </div>

            </fieldset>
        </s:form>


    </s:layout-component>
</s:layout-render>

