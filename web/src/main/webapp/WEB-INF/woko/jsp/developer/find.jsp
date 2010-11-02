<%@ page import="woko2.Woko" %>
<%@ page import="woko2.persistence.ObjectStore" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    Woko woko = Woko.getWoko(application);
    ObjectStore store = woko.getObjectStore();
%>
<w:facet facetName="layout"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="Find objects">
    <s:layout-component name="sidebarLinks">
        <ul class="menu">
            <li><a href="#">Help</a></li>
        </ul>
    </s:layout-component>
    <s:layout-component name="body">
        <h1>Find objects by class</h1>
        <p>
            Select the name of the class and submit :
        </p>
        <s:form action="/list">
            <s:select name="className">
                <s:options-collection collection="${find.mappedClasses}"/>
            </s:select>
            <s:submit name="find"/>
        </s:form>
    </s:layout-component>
</s:layout-render>