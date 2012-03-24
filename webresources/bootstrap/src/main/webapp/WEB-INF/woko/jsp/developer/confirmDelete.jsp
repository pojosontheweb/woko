<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.WokoFacets" %>

<c:set var="o" value="${actionBean.object}"/>
<w:facet facetName="<%=WokoFacets.layout%>" targetObject="${o}"/>
<w:facet targetObject="${o}" facetName="<%=WokoFacets.renderTitle%>"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${renderTitle.title}">
    <s:layout-component name="body">
        <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.devel.confirmDelete.title"/></h1>
        <p>
            <fmt:message bundle="${wokoBundle}" key="woko.devel.confirmDelete.question">
                <fmt:param value="${renderTitle.title}"/>
            </fmt:message>
            
            <w:objectClassName var="className" object="${actionBean.object}"/>
            <w:objectKey var="key" object="${actionBean.object}"/>
        </p>
        <%--<form action="${pageContext.request.contextPath}/delete/${className}/${key}" method="POST">--%>
            <%--<input type="submit" name="facet.confirm" value="Delete"/>--%>
            <%--<input type="submit" name="facet.cancel" value="Cancel"/>--%>
        <%--</form>--%>

        <s:form action="${pageContext.request.contextPath}/delete/${className}/${key}" class="form-inline">
            <s:submit name="facet.confirm" class="btn btn-primary"/>
            <s:submit name="facet.cancel" class="btn"/>
        </s:form>

    </s:layout-component>
</s:layout-render>
