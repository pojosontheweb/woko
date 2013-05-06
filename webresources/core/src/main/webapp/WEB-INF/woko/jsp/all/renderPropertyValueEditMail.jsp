<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.all.RenderPropertyValueEditMail" %>
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
    RenderPropertyValueEditMail renderFacets = (RenderPropertyValueEditMail)request.getAttribute(WokoFacets.renderPropertyValueEdit_email);

    if(renderFacets==null){
        renderFacets =  (RenderPropertyValueEditMail)request.getAttribute(WokoFacets.renderPropertyValueEdit_mail);
    }

    String mail = (String)renderFacets.getPropertyValue()!=null?(String)renderFacets.getPropertyValue():"";

    String titleExample = (String) renderFacets.getTitleExample();

    String fullFieldName = renderFacets.getFieldPrefix() + "." + renderFacets.getPropertyName();
%>
<input type="email" name="<%=fullFieldName%>" value="<%=mail%>"  title="<%=titleExample%>" />