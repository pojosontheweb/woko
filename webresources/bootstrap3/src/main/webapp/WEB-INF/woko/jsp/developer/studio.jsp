<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<c:set var="cp" value="${pageContext.request.contextPath}"/>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<%
    Woko<?,?,?,?> woko = Woko.getWoko(application);
%>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.studio.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}" bodyClass="claro">

    <s:layout-component name="customJs">
        <script type="text/javascript" src="${cp}/woko/js/woko.base.js"></script>
        <script type="text/javascript" src="${cp}/woko/js/woko.jquery.js"></script>
        <script type="text/javascript" src="${cp}/woko/js/woko.rpc.js"></script>

        <script type="text/javascript" src="${cp}/plugins/dataTables/jquery.dataTables.min.js"></script>
        <script type="text/javascript" src="${cp}/plugins/dataTables/DT_bootstrap.js"></script>
    </s:layout-component>

    <s:layout-component name="customCss">
        <link href="${cp}/plugins/dataTables/jquery.dataTables.css" type="text/css" rel="stylesheet"/>
    </s:layout-component>


    <s:layout-component name="body">

        <script type="text/javascript">

            function replaceAll(txt, replace, with_this) {
                return txt.replace(new RegExp(replace, 'g'),with_this);
            }

            $(function() {
                var hash = $(location).attr("hash");
                if (!hash) {
                    hash = ("#configuration");
                }

                $(".nav li").each(function() {
                    var li = $(this);
                    li.removeClass("active");
                    var a = $("a", li);
                    var href = a.attr("href");
                    if (hash==href) {
                        li.addClass("active");
                    }
                });
                $(".tab-pane").each(function() {
                    var tab = $(this);
                    tab.removeClass("active");
                    var tabId = "#"+tab.attr("id");
                    if (tabId == hash){
                        tab.addClass("active");
                    }
                })
            });


            $(document).ready(function(){

                var tbl = $("#tblFacets").dataTable({
                    "bPaginate": false
                });
                $("#tblFacets_filter").remove();
                $('#filterFacetsInput').keyup(function(){
                    tbl.fnFilter( $(this).val() );
                });

                var klient = new woko.rpc.Client('${cp}');
                var log = $("#log");
                var btnExec = $("#btnExec");

                btnExec.click(function() {
                    btnExec.attr('disabled', true);
                    log.empty();
                    klient.invokeFacet('groovy', {
                        content: {
                            "facet.code": $("#groovyCode").val()
                        },
                        onSuccess: function(resp) {
                            log.append(replaceAll(resp.log, '\n', '<br/>'));
                            btnExec.attr('disabled', false);
                        },
                        onError: function(err) {
                            log.append('ERROR !' + err);
                            btnExec.attr('disabled', false);
                        }
                    });
                });
            });

        </script>

        <style type="text/css">
            #groovyCode {
                border: 1px solid #d3d3d3;
                height: 200px;
            }

            #tblFacets_wrapper {
                overflow-x: auto;
            }

            #filterFacetsForm {
                margin-top: 12px;
                margin-bottom: 12px;
            }


        </style>

        <div class="container">

            <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.title"/></h1>

            <div class="tabbable">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#configuration" data-toggle="tab">Configuration</a></li>
                    <li><a href="#facets" data-toggle="tab">Facets</a></li>
                    <li><a href="#groovyShell" data-toggle="tab">Groovy Shell</a></li>
                </ul>

                <div class="tab-content">

                    <%-- Configuration tab --%>
                    <div class="tab-pane active" id="configuration">
                        <h2><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.config.title"/> </h2>
                        <ul>
                            <li><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.config.objectStore"/> <strong><%=woko.getObjectStore().getClass().getName()%></strong></li>
                            <li><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.config.userManager"/> <strong><%=woko.getUserManager().getClass().getName()%></strong></li>
                            <li><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.config.fallbackRoles"/> <strong><%=woko.getFallbackRoles()%></strong></li>
                            <li><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.config.userStrategy"/> <strong><%=woko.getUsernameResolutionStrategy()%></strong></li>
                        </ul>
                    </div>

                    <%-- Facets tab --%>
                    <div class="tab-pane" id="facets">

                        <div class="row">
                            <div class="col-md-4 offset-md-8">
                                <div class="form-inline" id="filterFacetsForm">
                                    <div class="input-group">
                                        <input type="text" id="filterFacetsInput" class="form-control"/>
                                        <span class="input-group-addon">
                                            <i class="glyphicon glyphicon-filter"> </i>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <table id="tblFacets" class="table table-striped table-bordered table-condensed">
                                    <thead>
                                    <tr>
                                        <th>name</th>
                                        <th>role</th>
                                        <th>targetType</th>
                                        <th>facetClass</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${studio.facetDescriptors}" var="fd">
                                        <tr>
                                            <td>${fd.name}</td>
                                            <td>${fd.profileId}</td>
                                            <td>${fd.targetObjectType.name}</td>
                                            <td>${fd.facetClass.name}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                    <%-- Groovy shell tab --%>
                    <div class="tab-pane" id="groovyShell">
                        <h2>
                            <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.title"/>
                        </h2>
                        <ul>
                            <li><strong><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.request"/> </strong> <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.requestType"/></li>
                            <li><strong><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.woko"/></strong> <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.wokoType"/></li>
                            <li><strong><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.logs"/></strong> <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.logsType"/></li>
                        </ul>

                        <div class="row">
                            <div class="col-md-12">
                                <textarea id="groovyCode" class="form-control">// groovy goes here !
log << "this runs on the server : $woko"</textarea>
                            </div>
                        </div>

                        <button id="btnExec" class="btn btn-primary">Execute</button>
                        <div>
                            <h2><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.log.exec"/></h2>
                            <div id="log" class="well"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </s:layout-component>
</s:layout-render>