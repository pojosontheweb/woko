<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.facets.builtin.*" %>
<%@ page import="woko.ext.usermanagement.facets.registration.ViewRegistrationGuest" %>
<%@ page import="woko.ext.usermanagement.core.RegistrationDetails" %>
<%@ page import="woko.ext.usermanagement.core.User" %>
<%@ page import="woko.ext.usermanagement.core.AccountStatus" %>
<w:facet facetName="<%=Layout.FACET_NAME%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.post.register.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">

        <%
            ViewRegistrationGuest view = (ViewRegistrationGuest)request.getAttribute(View.FACET_NAME);
            @SuppressWarnings("unchecked")
            RegistrationDetails<User> registrationDetails = (RegistrationDetails<User>)view.getFacetContext().getTargetObject();

            String alertClass,username,email;
            String msgKeyAlert = "woko.ext.usermanagement.post.register.alert.";
            String msgKeyPara = "woko.ext.usermanagement.post.register.para.";

            User u = registrationDetails.getUser();
            if (u!=null) {
                username = u.getUsername();
                email = u.getEmail();
                AccountStatus as = u.getAccountStatus();
                switch (as) {
                    case Registered:
                        alertClass = "alert-warning";
                        msgKeyAlert += "registered";
                        msgKeyPara += "registered";
                        break;
                    case Active:
                        alertClass = "alert-success";
                        msgKeyAlert += "active";
                        msgKeyPara += "active";
                        break;
                    case Blocked:
                        alertClass = "alert-error";
                        msgKeyAlert += "blocked";
                        msgKeyPara += "blocked";
                        break;
                    default: alertClass="";
                }
            } else {
                // already activated, grab activation infos
                username = registrationDetails.getActivatedUsername();
                email = registrationDetails.getActivatedEmail();
                alertClass = "alert-warning";
                msgKeyAlert += "registered";
                msgKeyPara += "registered";
            }
        %>

        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.post.register.h1.text">
                <fmt:param value="<%=username%>"/>
            </fmt:message>
        </h1>

        <div class="alert <%=alertClass%>">
            <fmt:message bundle="${wokoBundle}" key="<%=msgKeyAlert%>"/>
        </div>

        <fmt:message bundle="${wokoBundle}" key="<%=msgKeyPara%>">
            <fmt:param value="<%=email%>"/>
        </fmt:message>

    </s:layout-component>
</s:layout-render>