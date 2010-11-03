<%@ page import="woko2.Woko" %>
<%@ page import="net.sourceforge.jfacets.FacetDescriptor" %>
<%@ page import="woko2.facets.builtin.developer.WokoStudio" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<w:facet facetName="layout"/>
<%
    Woko woko = Woko.getWoko(application);
%>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="Studio">
    <s:layout-component name="sidebarLinks">
        <ul class="menu">
            <li><a href="#">Help</a></li>
        </ul>
    </s:layout-component>
    <s:layout-component name="body">
        <h1>Woko Studio</h1>
        <h2>Configured components</h2>
        <ul>
            <li>Object Store : <strong><%=woko.getObjectStore().getClass().getName()%></strong></li>
            <li>User Manager : <strong><%=woko.getUserManager().getClass().getName()%></strong></li>
            <li>Fallback Roles : <strong><%=woko.getFallbackRoles()%></strong></li>
        </ul>
        <h2>Registered Facets</h2>
        <table class="wokoFacets" cellpadding="0" cellspacing="0">
            <thead>
            <tr>
                <th>name</th>
                <th>role</th>
                <th>target type</th>
                <th>facet class</th>
            </tr>
            </thead>
            <tbody>
            <%
                WokoStudio studio = (WokoStudio)request.getAttribute("studio");
                int index = 0;
                for (FacetDescriptor fd : studio.getFacetDescriptors()) {
                    String rowCssClass = index++ % 2 == 0 ? "even" : "odd";  
            %>
                <tr class="<%=rowCssClass%>">
                    <td><%=fd.getName()%></td>
                    <td><%=fd.getProfileId()%></td>
                    <td><%=fd.getTargetObjectType().getName()%></td>
                    <td><%=fd.getFacetClass().getName()%></td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </s:layout-component>
</s:layout-render>