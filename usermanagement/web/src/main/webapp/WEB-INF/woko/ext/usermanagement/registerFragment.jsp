<%@ page import="net.sourceforge.stripes.util.CryptoUtil" %>
<%@ page import="woko.ext.usermanagement.facets.registration.RegisterGuest" %>
<%@ page import="woko.ext.usermanagement.facets.registration.RegisterFragmentGuest" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%
    RegisterFragmentGuest renderRegisterProperties = (RegisterFragmentGuest)request.getAttribute(RegisterFragmentGuest.FACET_NAME);
    String encryptedSourcePage = CryptoUtil.encrypt(RegisterFragmentGuest.FRAGMENT_PATH);
%>

<s:form action="/register" class="form-horizontal">
    <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>

    <fieldset>

        <div class="control-group ${empty(actionBean.context.validationErrors['facet.username']) ? '' : 'error'} ">
            <s:label for="facet.username" class="control-label wokoPropertyName">
                <fmt:message bundle="${wokoBundle}" key="User.username"/>
            </s:label>
            <div class="controls">
                <div class="input-prepend">
                    <span class="add-on"><i class="icon-user"> </i></span>
                    <s:text name="facet.username" class="input-xlarge"/>
                </div>
                <s:errors field="facet.username"/>
            </div>
        </div>

        <div class="control-group ${empty(actionBean.context.validationErrors['facet.email']) ? '' : 'error'} ">
            <s:label for="facet.email" class="control-label wokoPropertyName">
                <fmt:message bundle="${wokoBundle}" key="User.email"/>
            </s:label>
            <div class="controls">
                <div class="input-prepend">
                    <span class="add-on">@</span>
                    <s:text name="facet.email" class="input-xlarge"/>
                </div>
                <s:errors field="facet.email"/>
            </div>
        </div>

        <div class="control-group ${empty(actionBean.context.validationErrors['facet.password1']) ? '' : 'error'} ">
            <s:label for="facet.password1" class="control-label wokoPropertyName">
                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password1"/>
            </s:label>
            <div class="controls">
                <div class="input-prepend">
                                            <span class="add-on">
                                                <i class="icon-lock"> </i>
                                            </span>
                    <s:password name="facet.password1" class="input-xlarge"/>
                </div>
                <s:errors field="facet.password1"/>
            </div>
        </div>

        <div class="control-group ${empty(actionBean.context.validationErrors['facet.password2']) ? '' : 'error'} ">
            <s:label for="facet.password2" class="control-label wokoPropertyName">
                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password2"/>
            </s:label>
            <div class="controls">
                <div class="input-prepend">
                                            <span class="add-on">
                                                <i class="icon-lock"> </i>
                                            </span>
                    <s:password name="facet.password2" class="input-xlarge"/>
                </div>
                <s:errors field="facet.password2"/>
            </div>
        </div>

        <%-- Include captcha --%>
        <c:if test="${registerFragment.useCaptcha}">
            <div class="control-group">
                <s:label for="recaptcha_challenge_field" class="control-label wokoPropertyName">
                    <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.captcha"/>
                </s:label>
                <div class="controls">
                    <%
                        ReCaptcha c = ReCaptchaFactory.newReCaptcha(renderRegisterProperties.getReCaptchaPublicKey(), renderRegisterProperties.getReCaptchaPrivateKey(), false);
                    %>
                    <%=c.createRecaptchaHtml(null, null)%>
                </div>
            </div>
        </c:if>

        <w:includeFacet targetObject="${registerFragment.user}" facetName="<%=WokoFacets.renderPropertiesEdit%>"/>

        <div class="form-actions">
            <s:submit name="doRegister" class="btn btn-primary btn-large"/>
        </div>

    </fieldset>
</s:form>