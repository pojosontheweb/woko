<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<c:set var="user" value="${renderPropertyValueEdit.owningObject}"/>
<s:text name="facet.roles" class="input-xxlarge" value="${renderPropertyValueEdit.rolesStr}"/>
<span class="help-block">
    <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.user.roles.edit.help.text"/>
</span>
