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
        <h1>Find objects</h1>
        <p>
            This page allows to find objects from the ObjectStore. It allows to find by type, or by passing a
            native query. The results returned depend on the store implementation.
        </p>
        <p>
            Store used : <strong><%=store.getClass()%></strong>
        </p>
        <h2>Find by class</h2>
        <p>
            Type the name of the class (e.g. <i>Book</i>) and submit :
        </p>
        <s:form action="/list">
            <s:select name="className">
                <s:options-collection collection="${find.mappedClasses}"/>
            </s:select>
            <s:submit name="find"/>
        </s:form>
        <h2>Find by query</h2>
        <p>
            Type the native query (e.g. HQL if you use hibernate) and submit :
        </p>
        <s:form action="/query">
            <s:textarea name="query"/>
            <s:submit name="find"/>
        </s:form>
    </s:layout-component>
</s:layout-render>