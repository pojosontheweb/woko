<%@ page import="woko.ext.usermanagement.facets.registration.RegisterFragmentGuest" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%
    RegisterFragmentGuest renderRegisterProperties = (RegisterFragmentGuest)request.getAttribute(RegisterFragmentGuest.FACET_NAME);
%>
<s:form action="/register" class="form-horizontal">

    <w:b3-form-group-css fieldName="facet.username" var="formGroupCss"/>
    <div class="${formGroupCss}">
        <s:label for="facet.username" class="control-label col-sm-3">
            <fmt:message bundle="${wokoBundle}" key="User.username"/>
        </s:label>
        <div class="col-sm-4">
            <div class="input-group">
                <div class="input-group-addon">
                    <span class="glyphicon glyphicon-user"> </span>
                </div>
                <s:text name="facet.username" class="form-control"/>
            </div>
            <s:errors field="facet.username"/>
        </div>
    </div>

    <w:b3-form-group-css fieldName="facet.email" var="formGroupCss"/>
    <div class="${formGroupCss}">
        <s:label for="facet.email" class="control-label col-sm-3">
            <fmt:message bundle="${wokoBundle}" key="User.email"/>
        </s:label>
        <div class="col-sm-4">
            <div class="input-group">
                <div class="input-group-addon">
                    <i class="glyphicon glyphicon-envelope"> </i>
                </div>
                <s:text name="facet.email" class="form-control"/>
            </div>
            <s:errors field="facet.email"/>
        </div>
    </div>

    <w:b3-form-group-css fieldName="facet.password1" var="formGroupCss"/>
    <div class="${formGroupCss}">
        <s:label for="facet.password1" class="control-label col-sm-3">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password1"/>
        </s:label>
        <div class="col-sm-4">
            <div class="input-group">
                <div class="input-group-addon">
                    <i class="glyphicon glyphicon-lock"> </i>
                </div>
                <s:password name="facet.password1" class="form-control"/>
            </div>
            <s:errors field="facet.password1"/>
        </div>
    </div>

    <w:b3-form-group-css fieldName="facet.password2" var="formGroupCss"/>
    <div class="${formGroupCss}">
        <s:label for="facet.password2" class="control-label col-sm-3">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password2"/>
        </s:label>
        <div class="col-sm-4">
            <div class="input-group">
                <div class="input-group-addon">
                    <i class="glyphicon glyphicon-lock"> </i>
                </div>
                <s:password name="facet.password2" class="form-control"/>
            </div>
            <s:errors field="facet.password2"/>
        </div>
    </div>

    <c:if test="${registerFragment.useCaptcha}">
        <div class="form-group">
            <s:label for="recaptcha_challenge_field" class="control-label col-sm-3">
                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.captcha"/>
            </s:label>
            <div class="col-sm-9">
                <div class="" style="height: 150px">
                    <%
                        ReCaptcha c = ReCaptchaFactory.newReCaptcha(renderRegisterProperties.getReCaptchaPublicKey(), renderRegisterProperties.getReCaptchaPrivateKey(), false);
                    %>
                    <%=c.createRecaptchaHtml(null, null)%>
                </div>
            </div>
        </div>
    </c:if>

    <%--
    <w:includeFacet targetObject="${registerFragment.user}" facetName="<%=WokoFacets.renderPropertiesEdit%>"/>
    --%>

    <div class="form-group">
        <div class="col-sm-offset-3">
            <s:submit name="doRegister" class="btn btn-primary"/>
        </div>
    </div>

</s:form>