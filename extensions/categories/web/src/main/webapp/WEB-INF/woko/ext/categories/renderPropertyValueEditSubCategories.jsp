<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="java.util.Collection" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.ext.categories.facets.RenderPropertyValueEditSubCategoriesCategMgr" %>
<%@ page import="woko.ext.categories.Category" %>
<%@ page import="java.util.List" %>

<%
    RenderPropertyValueEditSubCategoriesCategMgr<?,?,?,?> renderPropertyValue = (RenderPropertyValueEditSubCategoriesCategMgr<?,?,?,?>)request.getAttribute(WokoFacets.renderPropertyValueEdit);
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)renderPropertyValue.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    ObjectStore os = woko.getObjectStore();
    List<Category> categories = (List<Category>)renderPropertyValue.getPropertyValue();

    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
%>
<span class="wokoPropertyValue">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <%
            if (categories!=null) {
        %>
        <table class="table table-condensed">
        <%
                for (Category c : categories) {
                    RenderPropertyValue nested = Util.getRenderPropValueFacet(woko, request, owningObject, propertyName, c);
        %>
            <div class="wokoCollectionItem">
                <jsp:include page="<%=nested.getFragmentPath(request)%>"/>
                <div class="btn-group">
                    <a class="btn">up</a>
                    <a class="btn">down</a>
                </div>
            </div>
        <%
                }
        %>
            </tbody>
        </table>
        <%
            }
        %>
    </span>
</span>


