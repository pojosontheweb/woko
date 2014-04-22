<%@ tag import="net.sourceforge.stripes.action.ActionBean" %>
<%@ tag import="net.sourceforge.stripes.action.ActionBeanContext" %>
<%@ tag import="net.sourceforge.stripes.validation.ValidationErrors" %>
<%@ tag import="net.sourceforge.stripes.validation.ValidationError" %>
<%@ tag import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="fieldName" required="true" type="java.lang.String" %>
<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="var" alias="daLink" scope="AT_END" %>
<%
    boolean hasErrors = false;
    ActionBean actionBean = (ActionBean)request.getAttribute("actionBean");
    ActionBeanContext context = actionBean.getContext();
    ValidationErrors errs = context.getValidationErrors();
    if (errs!=null) {
        List<ValidationError> errList = errs.get(fieldName);
        if (errList!=null && errList.size()>0) {
            hasErrors = true;
        }
    }
%>
<c:set var="daLink"><%=hasErrors%></c:set>




