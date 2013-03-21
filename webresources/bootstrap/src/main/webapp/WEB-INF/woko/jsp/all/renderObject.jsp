<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderObject" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.persistence.ObjectStore" %>

<%
    RenderObject renderObject = (RenderObject)request.getAttribute(WokoFacets.renderObject);
    Object o = renderObject.getFacetContext().getTargetObject();
    Class<?> c = o.getClass();
    ObjectStore store = Woko.getWoko(application).getObjectStore();
    String className = store.getClassMapping(store.getObjectClass(c));
%>
<div class="wokoObject <%=className%>">

    <%-- Display title and wokoLinks in the same row --%>
    <div class="row-fluid">
        <%-- Call the renderTitle facet in order to display the title --%>
        <div class="wokoTitle">
            <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderTitle%>"/>
        </div>

        <%-- Call the renderTitle facet in order to display the available links --%>
        <div class="wokoLinks pull-right">
            <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderLinks%>"/>
        </div>
    </div>

    <%-- Call the renderTitle facet in order to display the properties --%>
    <div class="wokoProperties">
        <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderProperties%>"/>
    </div>

</div>