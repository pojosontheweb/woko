<%@ page import="woko2.facets.builtin.RenderProperties" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ page import="woko2.Woko" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.facets.builtin.RenderPropertyName" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ page import="woko2.facets.builtin.RenderPropertyValue" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    RenderProperties editProperties = (RenderProperties)request.getAttribute("renderPropertiesEdit");
    List<String> propertyNames = editProperties.getPropertyNames();
    Map<String,Object> propertyValues = editProperties.getPropertyValues();
    WokoFacetContext fctx = (WokoFacetContext)editProperties.getContext();
    Woko woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
    ObjectStore os = woko.getObjectStore();
    String mappedClassName = os.getClassMapping(owningObject.getClass());
    String formUrl = "/save/" + mappedClassName;
    String key = os.getKey(owningObject);
    if (key!=null) {
        formUrl += "/" + key;
    }
%>
<div class="wokoPropertiesEdit">
    <s:form action="<%=formUrl%>">
        <table>
            <tbody>
            <%
                for (String pName : propertyNames) {
                    Object pVal = propertyValues.get(pName);

                    RenderPropertyName renderPropertyName =
                        (RenderPropertyName)woko.getFacet(RenderPropertyName.name, request, owningObject, owningObject.getClass(), true);
                    renderPropertyName.setPropertyName(pName);
                    String pNameFragmentPath = renderPropertyName.getFragmentPath(request);

                    RenderPropertyValue editPropertyValue = Util.getRenderPropValueEditFacet(woko, request, owningObject, pName, pVal);
                    String pValFragmentPath = editPropertyValue.getFragmentPath(request);
            %>
            <tr>
                <th><jsp:include page="<%=pNameFragmentPath%>"/></th>
                <td><jsp:include page="<%=pValFragmentPath%>"/></td>
            </tr>
            <%
                }
            %>
                <tr><td class="wokoButtonRow" colspan="2"><s:submit name="save"/></td></tr>
            </tbody>
        </table>
    </s:form>
</div>