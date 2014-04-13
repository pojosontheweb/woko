<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp" %>
<%@ page import="woko.facets.builtin.RenderObject" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.RenderPropertiesBefore" %>
<%@ page import="woko.facets.builtin.RenderPropertiesAfter" %>
<%
    RenderObject renderObject = (RenderObject) request.getAttribute(WokoFacets.renderObject);
    Object o = renderObject.getFacetContext().getTargetObject();
    Class<?> c = o.getClass();
    Woko<?, ?, ?, ?> woko = Woko.getWoko(application);
    ObjectStore store = woko.getObjectStore();
    String className = store.getClassMapping(store.getObjectClass(c));
%>
<div class="w-object <%=className%>">

    <%-- Display title and wokoLinks in the same row --%>
    <div class="container">
        <div class="row">
            <%-- Call the renderTitle facet in order to display the title --%>
            <div class="w-title col-lg-10 col-sm-10">
                <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderTitle%>"/>
            </div>

            <%-- Call the renderTitle facet in order to display the available links --%>
            <div class="w-links col-lg-2 col-sm-2">
                <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderLinks%>"/>
            </div>
        </div>
    </div>

    <%-- before properties if any --%>
    <w:includeFacet targetObject="<%=o%>"
                    facetName="<%=RenderPropertiesBefore.FACET_NAME%>"
                    throwIfNotFound="false"/>

    <%-- Call the renderTitle facet in order to display the properties --%>
    <w:includeFacet targetObject="<%=o%>" facetName="<%=WokoFacets.renderProperties%>"/>

    <%-- after properties if any --%>
    <w:includeFacet targetObject="<%=o%>"
                    facetName="<%=RenderPropertiesAfter.FACET_NAME%>"
                    throwIfNotFound="false"/>
</div>