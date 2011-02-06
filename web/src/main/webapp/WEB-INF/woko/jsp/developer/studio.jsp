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
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="Studio" bodyClass="claro">


    <s:layout-component name="sidebarLinks">
        <ul class="menu">
            <li><a href="#">Help</a></li>
        </ul>
    </s:layout-component>

    <s:layout-component name="body">

        <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/dojo/1.5/dojo/resources/dojo.css">
        <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/dojo/1.5/dijit/themes/claro/claro.css" />
        <style type="text/css">
            @import "http://ajax.googleapis.com/ajax/libs/dojo/1.5/dojox/grid/resources/Grid.css";
            @import "http://ajax.googleapis.com/ajax/libs/dojo/1.5/dojox/grid/resources/claroGrid.css";
            .dojoxGrid table {
                margin: 0;
                font-family:helvetica,arial,sans-serif; font-size:90%;
            }
            html, body { width: 100%; height: 100%;
            margin: 0; }
        </style>

        <script src="http://www.google.com/jsapi"></script>
        <script>google.load("dojo", "1.5");</script>
        <script>
            function loader () {
                dojo.require("dijit.form.Button");
                dojo.require("dojox.grid.DataGrid");
                dojo.require("dojox.data.HtmlTableStore");

                dojo.addOnLoad(callback);
            }

            function callback () {
                var store = new dojox.data.HtmlTableStore({tableId:"facetsTable"});

                var layout = [{
                    field: 'name',
                    name: 'Name',
                    width: '150px'
                },
                {
                    field: 'role',
                    name: 'Role',
                    width: '100px'
                },
                {
                    field: 'targetType',
                    name: 'Target type',
                    width: '150px'
                },
                {
                    field: 'facetClass',
                    name: 'Facet class',
                    width: 'auto'
                }];

                // create a new grid:
                var grid = new dojox.grid.DataGrid({
                    query: {
                        name: '*'
                    },
                    store: store,
                    clientSort: true,
//                    rowSelector: '20px',
                    structure: layout,
                    autoHeight: 20

                },
                document.createElement('div'));
                dojo.byId("gridContainer").appendChild(grid.domNode);

                grid.startup();
            }

            dojo.config.parseOnLoad = true;
            dojo.config.dojoBlankHtmlUrl = '/blank.html';
            dojo.addOnLoad(loader);
        </script>

        <h1>Woko Studio</h1>
        <h2>Configuration</h2>
            <ul>
                <li>Object Store : <strong><%=woko.getObjectStore().getClass().getName()%></strong></li>
                <li>User Manager : <strong><%=woko.getUserManager().getClass().getName()%></strong></li>
                <li>Fallback Roles : <strong><%=woko.getFallbackRoles()%></strong></li>
            </ul>
        <h2>Facets</h2>
        <div id="gridContainer" style="width: 100%; height: 400px;"></div>
        <table id="facetsTable" style="display:none;">
            <thead>
            <tr>
                <th>name</th>
                <th>role</th>
                <th>targetType</th>
                <th>facetClass</th>
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