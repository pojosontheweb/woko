<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderObject" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.persistence.ObjectStore" %>

<%
    RenderObject renderObject = (RenderObject)request.getAttribute(WokoFacets.renderObjectEdit);
    Object o = renderObject.getFacetContext().getTargetObject();
    ObjectStore s = Woko.getWoko(application).getObjectStore();
    String className = s.getClassMapping(s.getObjectClass(o));
%>
<div class="wokoObject <%=className%>">

    <%-- Display title and wokoLinks in the same row --%>
    <div class="pure-g-r">
        <%-- Call the renderTitle facet in order to display the title --%>
        <div class="wokoTitle pure-u-1-2">
            <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderTitleEdit%>"/>
        </div>
        <%-- Call the renderTitle facet in order to display the available links --%>
        <div class="wokoLinks pure-u-1-2">
            <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderLinksEdit%>"/>
        </div>
    </div>

    <%-- Call the renderTitle facet in order to display the title properties --%>
    <div class="wokoPropertiesEdit">
        <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderPropertiesEdit%>"/>
    </div>

</div>