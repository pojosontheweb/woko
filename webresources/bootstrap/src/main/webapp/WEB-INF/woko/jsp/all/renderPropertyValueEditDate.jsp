<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="java.util.Locale" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute(WokoFacets.renderPropertyValueEdit);
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext<?,?,?,?>)renderPropertyValue.getFacetContext();
    ObjectStore os = fctx.getWoko().getObjectStore();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    String owningClass = os.getClassMapping(owningObject.getClass());
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
    String fullFieldName = "object." + propertyName;
    String fieldId = "dp-" + owningClass + "-" + propertyName;
    Locale locale = request.getLocale();
    String localeStr = locale!=null ? locale.toString() : "";
%>
<span class="wokoPropertyValueEdit">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <s:text name="<%=fullFieldName%>" id="<%=fieldId%>"/>
    </span>
</span>
<script type="text/javascript">
    $(document).ready(function() {
        $("#<%=fieldId%>").datepicker($.datepicker.regional['<%=localeStr%>']);
    })
</script>


