<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ attribute name="fieldName" required="true" type="java.lang.String" %>
<%@ attribute name="additionalCss" required="false" type="java.lang.String" %>
<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="var" alias="daCss" scope="AT_END" %>
<c:set var="fullCss" value="form-group"/>
<w:fieldHasError fieldName="${pageScope.fieldName}" var="hasErrors"/>
<c:if test="${hasErrors}">
    <c:set var="fullCss" value="${fullCss} has-error"/>
</c:if>
<c:if test="${pageScope.additionalCss!=null}">
    <c:set var="fullCss" value="${fullCss} ${pageScope.additionalCss}"/>
</c:if>
<c:set var="daCss" value="${fullCss}"/>



