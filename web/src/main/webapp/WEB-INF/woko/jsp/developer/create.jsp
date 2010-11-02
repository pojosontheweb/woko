<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<w:facet facetName="layout"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="Create object">
    <s:layout-component name="sidebarLinks">
        <ul class="menu">
            <li><a href="#">Help</a></li>
        </ul>
    </s:layout-component>
    <s:layout-component name="body">
        <h1>Create a new object</h1>
        <p>
            Select the the class of the object to create, and submit :
        </p>
        <s:form action="/save">
            <s:select name="className">
                <s:options-collection collection="${create.mappedClasses}"/>
            </s:select>
            <s:submit name="create"/>
        </s:form>
    </s:layout-component>
</s:layout-render>