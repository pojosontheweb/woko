<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="java.util.Collection" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute(WokoFacets.renderPropertyValue);
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)renderPropertyValue.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    ObjectStore os = woko.getObjectStore();
    Collection propertyValue = (Collection)fctx.getTargetObject();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    if (propertyValue!=null) {
        for (Object elem : propertyValue) {
            // reuse viewPropertyValue on element
            RenderPropertyValue nested = Util.getRenderPropValueFacet(woko, request, owningObject, propertyName, elem);
%>
        <div class="w-collection-item">
            <jsp:include page="<%=nested.getFragmentPath(request)%>"/>
        </div>
<%
        }
    }
%>

