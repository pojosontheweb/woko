<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderObject" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<%
    RenderObject renderObject = (RenderObject)request.getAttribute(WokoFacets.renderObject);
    Object o = renderObject.getFacetContext().getTargetObject();
    Class<?> c = o.getClass();
    String className = Woko.getWoko(application).getObjectStore().getClassMapping(c);
%>
<div class="wokoObject <%=className%>">
    <div class="row-fluid">
        <div class="wokoTitle">
            <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderTitle%>"/>
        </div>
        <div class="wokoLinks pull-right">
            <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderLinks%>"/>
        </div>
    </div>
    <div class="row-fluid">
        <div class="wokoProperties">
            <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderProperties%>"/>
        </div>
    </div>
</div>