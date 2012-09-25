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

        <div class="row-fluid">
            <div class="span12">
                <s:form action="/register" class="form-horizontal">
                    <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>
                    <fieldset>

                        <div class="control-group ${empty(actionBean.context.validationErrors['facet.username']) ? '' : 'error'} ">
                            <s:label for="facet.username" class="control-label">
                                <fmt:message bundle="${wokoBundle}" key="User.username"/>
                            </s:label>
                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on"><i class="icon-user"> </i></span>
                                    <s:text name="facet.username" class="span4"/>
                                </div>
                                <s:errors field="facet.username"/>
                            </div>
                        </div>

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

                        <div class="control-group ${empty(actionBean.context.validationErrors['facet.password1']) ? '' : 'error'} ">
                            <s:label for="facet.password1" class="control-label">
                                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password1"/>
                            </s:label>
                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on">
                                        <i class="icon-lock"> </i>
                                    </span>
                                    <s:password name="facet.password1" class="span4"/>
                                </div>
                                <s:errors field="facet.password1"/>
                            </div>
                        </div>

                        <div class="control-group ${empty(actionBean.context.validationErrors['facet.password2']) ? '' : 'error'} ">
                            <s:label for="facet.password2" class="control-label">
                                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password2"/>
                            </s:label>
                            <div class="controls">
                                <div class="input-prepend">
                                    <span class="add-on">
                                        <i class="icon-lock"> </i>
                                    </span>
                                    <s:password name="facet.password2" class="span4"/>
                                </div>
                                <s:errors field="facet.password2"/>
                            </div>
                        </div>

                        <%-- render the properties FORM for the user --%>
                        <w:includeFacet targetObject="${register.user}" facetName="<%=WokoFacets.renderPropertiesEdit%>"/>

                        <div class="form-actions">
                            <s:submit name="doRegister" class="btn btn-primary btn-large"/>
                        </div>

                    </fieldset>
                </s:form>
            </div>
        </div>

    </s:layout-component>
</s:layout-render>

