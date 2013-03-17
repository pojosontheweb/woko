<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.ext.blobs.facets.RenderPropertyValueBlobInputStream" %>
<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.util.LinkUtil" %>
<%@ page import="woko.ext.blobs.facets.DownloadBlob" %>

<%
    RenderPropertyValueBlobInputStream<?,?,?,?> renderPropertyValue = (RenderPropertyValueBlobInputStream<?,?,?,?>)request.getAttribute(RenderPropertyValue.FACET_NAME);
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)renderPropertyValue.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    Object  propertyValue = renderPropertyValue.getPropertyValue();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    Class<?> propertyClass = propertyValue!=null ?
            propertyValue.getClass() :
            Util.getPropertyType(owningObject.getClass(), propertyName);
%>
<span class="wokoPropertyValue">
    <span class="<%=propertyName%> <%=propertyClass.getName()%>">
        <c:if test="<%=propertyValue!=null%>">
            <%
                String href = request.getContextPath() + "/" + LinkUtil.getUrl(woko, owningObject, DownloadBlob.FACET_NAME);
            %>
            <a href="<%=href%>?facet.attachment=true" class="btn">
                <fmt:message bundle="${wokoBundle}" key="woko.ext.blobs.download.link"/>
            </a>
        </c:if>
    </span>
</span>


