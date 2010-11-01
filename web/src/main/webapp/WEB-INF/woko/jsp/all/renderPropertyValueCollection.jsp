<%@ page import="woko2.facets.builtin.all.RenderPropertyValue" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ page import="java.util.Collection" %>
<%@ page import="woko2.Woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute("renderPropertyValue");
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getContext();
    Woko woko = fctx.getWoko();
    ObjectStore os = woko.getObjectStore();
    Collection propertyValue = (Collection)fctx.getTargetObject();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
%>
<span class="wokoPropertyValue">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <%
            for (Object elem : propertyValue) {
                // reuse viewPropertyValue on element
                RenderPropertyValue nested = (RenderPropertyValue)woko.getFacet(
                        RenderPropertyValue.name,
                        request,
                        elem,
                        elem==null ? null : elem.getClass());
                nested.setOwningObject(owningObject);
                nested.setPropertyName(propertyName);
        %>
            <div class="wokoCollectionItem">
                <jsp:include page="<%=nested.getFragmentPath(request)%>"/>
            </div>
        <%
            }
        %>
    </span>
</span>


