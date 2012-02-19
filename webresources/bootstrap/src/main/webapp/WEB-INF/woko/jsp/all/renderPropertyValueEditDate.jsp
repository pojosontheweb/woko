<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="java.util.Locale" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute("renderPropertyValueEdit");
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getFacetContext();
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


