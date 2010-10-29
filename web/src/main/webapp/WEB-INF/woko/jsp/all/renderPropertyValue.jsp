<%@ page import="woko2.facets.builtin.all.RenderPropertyValue" %>
<%@ page import="woko2.util.Util" %>
<%@ page import="woko2.facets.WokoFacetContext" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ page import="woko2.Woko" %>
<%@ page import="woko2.facets.FacetConstants" %>
<%@ page import="woko2.facets.builtin.all.RenderTitle" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    RenderPropertyValue renderPropertyValue = (RenderPropertyValue)request.getAttribute("renderPropertyValue");
    WokoFacetContext fctx = (WokoFacetContext)renderPropertyValue.getContext();
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
            String key = os.getKey(propertyValue);
            if (key!=null) {
                // we have a className and a key, can the user view the object ?
                if (woko.getFacet(FacetConstants.view, request, propertyValue, propertyClass)!=null) {
                    href = request.getContextPath() + "/view/" + propertyClassName + "/" + key;
                    RenderTitle rt = (RenderTitle)woko.getFacet(FacetConstants.renderTitle, request, propertyValue, propertyClass, true);
                    linkTitle = rt.getTitle();
                }
            }
        }
    } else {
        propertyClassName = propertyClass.getName();
    }
%>
<span class="wokoPropertyValue">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <c:choose>
            <c:when test="<%=href!=null%>">
                <a href="<%=href%>"><c:out value="<%=linkTitle%>"/></a>
            </c:when>
            <c:otherwise>
                <c:out value="<%=propertyValue%>"/>
            </c:otherwise>
        </c:choose>
    </span>
</span>


