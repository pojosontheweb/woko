<%@ page import="woko.Woko" %>
<%@ page import="net.sourceforge.jfacets.FacetDescriptor" %>
<%@ page import="woko.facets.builtin.developer.WokoStudio" %>
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
            }
            .dijitContentPane {
                font-size:0.8em;
            }

            .dijitTextarea {
                font-size:12pt;
                font-family: "courier new",sans-serif;
            }

            html, body { width: 100%; height: 100%;
            margin: 0; }

            .claro {
                font-family: Geneva, Arial, Helvetica, sans-serif;
                font-size: 1.0em;
            }

            body {
                font: 100%/150% Geneva, Arial, Helvetica, sans-serif;
            }
        </style>

        <script src="http://www.google.com/jsapi"></script>
        <script>google.load("dojo", "1.5");</script>
        <script>
            function loader () {
                dojo.require("dijit.form.Button");
                dojo.require("dojox.grid.DataGrid");
                dojo.require("dojox.data.HtmlTableStore");
                dojo.require("dijit.layout.TabContainer");
                dojo.require("dijit.layout.ContentPane");
                dojo.require("dijit.form.Textarea");
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
                    structure: layout
                },
                document.createElement('div'));
                dojo.byId("gridContainer").appendChild(grid.domNode);

                grid.startup();
            }

            dojo.config.parseOnLoad = true;
            dojo.config.dojoBlankHtmlUrl = '/blank.html';
            dojo.addOnLoad(loader);

            function writeLog(str) {
                dijit.byId('log').attr('value', str);
            }

            function execGroovy() {
                var code = dijit.byId('groovyCode').attr('value');
                dijit.byId('execute').attr('disabled', true);
                dojo.xhrPost({
                    url: "${pageContext.request.contextPath}/groovy",
                    handleAs: "json",
                    content : {
                        "facet.code": code
                    },
                    load: function(data) {
                        writeLog(data.log);
                        dijit.byId('execute').attr('disabled', false);
                    },
                    error: function(data) {
                        writeLog("ERROR !");
                        dijit.byId('execute').attr('disabled', false);
                    }
                });
            }
        </script>

        <h1>Woko Studio</h1>

        <div style="width: 100%; height: 600px">
            <div dojoType="dijit.layout.TabContainer" style="width: 100%; height: 100%;">
                <div dojoType="dijit.layout.ContentPane" title="Configuration" selected="true">
                    <h2>The following components are configured</h2>
                    <ul>
                        <li>Object Store : <strong><%=woko.getObjectStore().getClass().getName()%></strong></li>
                        <li>User Manager : <strong><%=woko.getUserManager().getClass().getName()%></strong></li>
                        <li>Fallback Roles : <strong><%=woko.getFallbackRoles()%></strong></li>
                        <li>Username resolution strategy : <strong><%=woko.getUsernameResolutionStrategy()%></strong></li>
                    </ul>
                </div>
                <div dojoType="dijit.layout.ContentPane" title="Facets">
                    <div id="gridContainer" style="width: 100%; height: 100%;"></div>
                </div>
                <div dojoType="dijit.layout.ContentPane" title="Groovy shell">
                    <h2>Variables at your disposal</h2>
                    <ul>
                        <li><strong>request</strong> (HttpServletRequest)</li>
                        <li><strong>woko</strong> (Woko)</li>
                        <li><strong>log(s)</strong> (log function)</li>
                    </ul>
                    <h2>Groovy code</h2>
                    <div dojoType="dijit.form.Textarea" id="groovyCode">Groovy Script here...<script type="dojo/method" event="onClick">
                            if (!window.clickedAlready) {
                                window.clickedAlready = true;
                                this.attr('value', '');
                            }
                        </script></div>
                    <button dojoType="dijit.form.Button" id="execute" onclick="execGroovy();">Execute</button>
                    <h2>Log</h2>
                    <div dojoType="dijit.form.Textarea" disabled="true" id="log">Exec log</div>
                </div>
            </div>
        </div>
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