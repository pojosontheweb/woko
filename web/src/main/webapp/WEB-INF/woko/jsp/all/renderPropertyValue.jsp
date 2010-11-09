<%@ page import="woko2.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ page import="woko2.Woko" %>
<%@ page import="woko2.facets.builtin.RenderTitle" %>
<%@ page import="woko2.facets.builtin.View" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute(RenderPropertyValue.name);
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getFacetContext();
    Woko woko = fctx.getWoko();
    ObjectStore os = fctx.getWoko().getObjectStore();
    Object propertyValue = fctx.getTargetObject();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    Class<?> propertyClass = propertyValue!=null ?
            propertyValue.getClass() :
            Util.getPropertyType(owningObject.getClass(), propertyName);

    String propertyMappedClassName = os.getClassMapping(propertyClass);
    String propertyClassName;
    String href = null;
    String linkTitle = null;
    if (propertyMappedClassName!=null) {
        propertyClassName = propertyMappedClassName;
        if (propertyValue!=null) {
            RenderTitle rt = (RenderTitle)woko.getFacet(RenderTitle.name, request, propertyValue, propertyClass, true);
            linkTitle = rt.getTitle();
            String key = os.getKey(propertyValue);
            if (key!=null) {
                // we have a className and a key, can the user view the object ?
                if (woko.getFacet(View.name, request, propertyValue, propertyClass)!=null) {
                    href = request.getContextPath() + "/view/" + propertyClassName + "/" + key;
                }
            }
        }
    } else {
        propertyClassName = propertyClass.getName();
    }
    Object propertyValueStr = linkTitle!=null ? linkTitle : propertyValue;
%>
<span class="wokoPropertyValue">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <c:choose>
            <c:when test="<%=href!=null%>">
                <a href="<%=href%>"><c:out value="<%=linkTitle%>"/></a>
            </c:when>
            <c:otherwise>
                <c:out value="<%=propertyValueStr%>"/>
            </c:otherwise>
        </c:choose>
    </span>
</span>


