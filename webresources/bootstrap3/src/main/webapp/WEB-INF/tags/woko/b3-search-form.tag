<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<s:form action="/search" class="form-inline" method="GET" role="search">
    <div class="input-group">
        <fmt:message bundle="${wokoBundle}" key="woko.devel.find.enterQuery" var="placeholder"/>
        <s:text name="facet.query" class="form-control" placeholder="${placeholder}"/>
        <span class="input-group-btn">
            <button class="btn btn-primary" type="submit" value="search">
                <i class="glyphicon glyphicon-search"> </i>
            </button>
        </span>
    </div>
</s:form>