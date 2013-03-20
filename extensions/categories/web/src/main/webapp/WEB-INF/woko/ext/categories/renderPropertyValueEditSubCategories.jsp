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
<%@ page import="woko.facets.builtin.Edit" %>
<%@ page import="woko.util.LinkUtil" %>
<%@ page import="woko.ext.categories.facets.MoveCategory" %>

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
        <table class="table table-bordered">
            <tbody>
        <%
                for (Category c : categories) {
                    RenderPropertyValue nested = Util.getRenderPropValueFacet(woko, request, owningObject, propertyName, c);
        %>
        <tr>
            <td>
                <jsp:include page="<%=nested.getFragmentPath(request)%>"/>
            </td>
            <td>
                <div class="btn-group">
                    <%
                        Edit edit = (Edit)woko.getFacet(Edit.FACET_NAME, request, c);
                        if (edit!=null) {
                            String editUrl = LinkUtil.getUrl(woko, c,  Edit.FACET_NAME);
                    %>
                            <a class="btn" href="${cp}/<%=editUrl%>"><fmt:message bundle="${wokoBundle}" key="woko.links.edit"/></a>
                    <%
                        }
                        MoveCategory move = (MoveCategory)woko.getFacet(MoveCategory.FACET_NAME, request,  c);
                        if (move!=null) {
                            String moveUrl = LinkUtil.getUrl(woko, c, MoveCategory.FACET_NAME);
                            if (move.isMoveUpAllowed()) {
                    %>
                                <a class="btn" href="${cp}/<%=moveUrl%>?facet.up=true"><fmt:message bundle="${wokoBundle}" key="woko.ext.categories.move.up"/></a>
                    <%

                            }
                            if (move.isMoveDownAllowed()) {
                    %>
                                <a class="btn" href="${cp}/<%=moveUrl%>?facet.up=false"><fmt:message bundle="${wokoBundle}" key="woko.ext.categories.move.down"/></a>
                    <%
                            }
                        }
                    %>
                </div>
            </td>
        </tr>
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


