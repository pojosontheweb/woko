<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.facets.builtin.RenderPropertyName" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="net.sourceforge.stripes.controller.StripesFilter" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="woko.facets.builtin.RenderPropertiesEdit" %>

<%
    RenderPropertyName renderPropertyName = (RenderPropertyName)request.getAttribute(WokoFacets.renderPropertyName);
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)renderPropertyName.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    ObjectStore os = woko.getObjectStore();
    String propertyName = renderPropertyName.getPropertyName();
    Object owningObject = fctx.getTargetObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
    String labelClass = "control-label wokoPropertyName " + propertyClassName + "-" + propertyName;

    // check in request if we have a renderPropertiesEdit facet that we could
    // use for prefix
    String prefix = "object";
    RenderPropertiesEdit rpe = (RenderPropertiesEdit)request.getAttribute(RenderPropertiesEdit.FACET_NAME);
    if (rpe!=null) {
        prefix = rpe.getFieldPrefix();
    }
    String label = prefix + "." + propertyName;
    String labelMsgKey = os.getClassMapping(os.getObjectClass(owningObject)) + "." + propertyName;
    ResourceBundle b = StripesFilter.getConfiguration().
                getLocalizationBundleFactory().getFormFieldBundle(request.getLocale());
    String msg = propertyName;
    try {
        msg = b.getString(labelMsgKey);
    } catch(MissingResourceException e) {
        // just let it through and use property name
    }
%>
<s:label for="<%=label%>" class="<%=labelClass%>"><c:out value="<%=msg%>"/></s:label>


