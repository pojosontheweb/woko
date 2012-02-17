<%@ page import="woko.facets.builtin.RenderObject" %>
<%@ page import="woko.Woko" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%
    RenderObject renderObject = (RenderObject)request.getAttribute("renderObject");
    Object o = renderObject.getFacetContext().getTargetObject();
    Class<?> c = o.getClass();
    String className = Woko.getWoko(application).getObjectStore().getClassMapping(c);
%>
<div class="wokoObject <%=className%> span12">
    <div class="row-fluid">
        <div class="wokoTitle span9">
            <w:includeFacet targetObject="<%=o%>" facetName="renderTitle"/>
        </div>
        <div class="wokoLinks span3">
            <w:includeFacet targetObject="<%=o%>" facetName="renderLinks"/>
        </div>
    </div>
    <div class="row-fluid">
        <div class="wokoProperties span12">
            <w:includeFacet targetObject="<%=o%>" facetName="renderProperties"/>
        </div>
    </div>
</div>