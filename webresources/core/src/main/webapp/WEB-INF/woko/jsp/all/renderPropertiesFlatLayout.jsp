<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderProperties" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<%
    RenderProperties renderProperties = (RenderProperties)request.getAttribute(WokoFacets.renderProperties);
    List<String> propertyNames = renderProperties.getPropertyNames();
    Map<String,Object> propertyValues = renderProperties.getPropertyValues();
    WokoFacetContext fctx = (WokoFacetContext)renderProperties.getFacetContext();
    Woko woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
    String owningObjectClassName = woko.getObjectStore().getClassMapping(owningObject.getClass());
%>
<div class="wokoProperties">
    <%
      for (String pName : propertyNames) {
          Object pVal = propertyValues.get(pName);
          RenderPropertyValue renderPropertyValue = Util.getRenderPropValueFacet(woko, request, owningObject, pName, pVal);
          String pValFragmentPath = renderPropertyValue.getFragmentPath(request);
    %>
      <div class="wokoProperty <%=owningObjectClassName%> <%=pName%>">
          <jsp:include page="<%=pValFragmentPath%>"/>
      </div>
    <%
      }
    %>
</div>