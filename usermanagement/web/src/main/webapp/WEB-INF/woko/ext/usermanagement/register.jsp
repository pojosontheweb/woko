<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="net.sourceforge.stripes.util.CryptoUtil" %>
<%@ page import="woko.ext.usermanagement.facets.registration.Register" %>
<%@ page import="woko.ext.usermanagement.facets.registration.RenderRegisterProperties" %>
<%@ page import="woko.facets.builtin.Layout" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
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

        <div class="row-fluid">
            <div class="span12">
                <s:form action="/register" class="form-horizontal">
                    <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>
                    <fieldset>

                        <%-- render the properties FORM for the basic registration --%>
                        <w:includeFacet facetName="<%=RenderRegisterProperties.FACET_NAME%>"/>

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

