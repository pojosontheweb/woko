<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.all.RenderPropertyValueEditPhone" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%--
  Created by IntelliJ IDEA.
  User: clem
  Date: 25/04/13
  Time: 18:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Woko<?,?,?,?> woko = Woko.getWoko(application);
    RenderPropertyValueEditPhone renderFacets = (RenderPropertyValueEditPhone)request.getAttribute(WokoFacets.renderPropertyValueEdit_phone);

    if(renderFacets==null){
        renderFacets =  (RenderPropertyValueEditPhone)request.getAttribute(WokoFacets.renderPropertyValueEdit_mobilePhone);
    }

    String phone =  (String)renderFacets.getPropertyValue()!=null?(String)renderFacets.getPropertyValue():"";
    String regex = (String) renderFacets.getPhoneRegex();
    String titleExample = (String) renderFacets.getTitleExample();

    String fullFieldName = renderFacets.getFieldPrefix() + "." + renderFacets.getPropertyName();
%>
<input type="tel" name="<%=fullFieldName%>" value="<%=phone%>" pattern="<%=regex%>" title="<%=titleExample%>" />