<%@ page import="woko.Woko" %>
<%@ page import="net.sourceforge.jfacets.FacetDescriptor" %>
<%@ page import="woko.facets.builtin.developer.WokoStudio" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="layout"/>
<%
    Woko woko = Woko.getWoko(application);
%>
<fmt:message var="pageTitle" key="woko.devel.studio.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" bodyClass="claro">


    <s:layout-component name="sidebarLinks">
        <ul class="menu">
            <li><a href="#"><fmt:message key="woko.actions.help"/> </a></li>
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

        <h1><fmt:message key="woko.devel.studio.title"/></h1>

        <div style="width: 100%; height: 600px">
            <div dojoType="dijit.layout.TabContainer" style="width: 100%; height: 100%;">
                <div dojoType="dijit.layout.ContentPane" title="Configuration" selected="true">
                    <h2><fmt:message key="woko.devel.studio.config.title"/> </h2>
                    <ul>
                        <li><fmt:message key="woko.devel.studio.config.objectStore"/> <strong><%=woko.getObjectStore().getClass().getName()%></strong></li>
                        <li><fmt:message key="woko.devel.studio.config.userManager"/> <strong><%=woko.getUserManager().getClass().getName()%></strong></li>
                        <li><fmt:message key="woko.devel.studio.config.fallbackRoles"/> <strong><%=woko.getFallbackRoles()%></strong></li>
                        <li><fmt:message key="woko.devel.studio.config.userStrategy"/> <strong><%=woko.getUsernameResolutionStrategy()%></strong></li>
                    </ul>
                </div>
                <div dojoType="dijit.layout.ContentPane" title="Facets">
                    <div id="gridContainer" style="width: 100%; height: 100%;"></div>
                </div>
                <div dojoType="dijit.layout.ContentPane" title="Groovy shell">
                    <h2><fmt:message key="woko.devel.studio.groovy.title"/> </h2>
                    <ul>
                        <li><strong><fmt:message key="woko.devel.studio.groovy.request"/> </strong> <fmt:message key="woko.devel.studio.groovy.requestType"/></li>
                        <li><strong><fmt:message key="woko.devel.studio.groovy.woko"/></strong> <fmt:message key="woko.devel.studio.groovy.wokoType"/></li>
                        <li><strong><fmt:message key="woko.devel.studio.groovy.logs"/></strong> <fmt:message key="woko.devel.studio.groovy.logsType"/></li>
                    </ul>
                    <h2><fmt:message key="woko.devel.studio.groovy.code"/> </h2>
                    <div dojoType="dijit.form.Textarea" id="groovyCode"><fmt:message key="woko.devel.studio.groovy.script"/><script type="dojo/method" event="onClick">
                            if (!window.clickedAlready) {
                                window.clickedAlready = true;
                                this.attr('value', '');
                            }
                        </script></div>
                    <button dojoType="dijit.form.Button" id="execute" onclick="execGroovy();"><fmt:message key="woko.devel.studio.groovy.execute"/> </button>
                    <h2><fmt:message key="woko.devel.studio.log.title"/> </h2>
                    <div dojoType="dijit.form.Textarea" disabled="true" id="log"><fmt:message key="woko.devel.studio.log.exec"/></div>
                </div>
            </div>
        </div>
        <table id="facetsTable" style="display:none;">
            <thead>
            <tr>
                <th><fmt:message key="woko.devel.studio.table.name"/> </th>
                <th><fmt:message key="woko.devel.studio.table.role"/></th>
                <th><fmt:message key="woko.devel.studio.table.targetType"/></th>
                <th><fmt:message key="woko.devel.studio.table.facetClass"/></th>
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