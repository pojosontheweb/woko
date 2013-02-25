<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.bootstrap.all.Theme" %>
<%@ page import="woko.facets.builtin.bootstrap.all.AlternativeLayout" %>
<%@ page import="woko.facets.builtin.bootstrap.all.SwitchLayout" %>

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

        <script src="${cp}/woko/ace/ace-uncompressed-noconflict.js" type="text/javascript" charset="utf-8"></script>
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
                    tab.removeClass("active")
                    var tabId = "#"+tab.attr("id")
                    if (tabId == hash){
                        tab.addClass("active")
                    }
                })
            });


            $(document).ready(function(){

                $("#tblFacets").dataTable({
                    "bPaginate": false
                });

                var klient = new woko.rpc.Client('${cp}');
                var log = $("#log");
                var btnExec = $("#btnExec");

                btnExec.click(function() {
                    btnExec.attr('disabled', true);
                    log.empty();
                    klient.invokeFacet('groovy', {
                        content: {
                            "facet.code": editor.getSession().getValue()
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

                // initialize code editor
                editor = ace.edit("groovyCode");
                editor.setTheme("ace/theme/textmate");
                editor.getSession().setMode("ace/mode/groovy");
                
            });

        </script>

        <style type="text/css">
            #groovyCodeWrapper {
                height: 300px;
                margin-bottom: 10px;
            }
            #groovyCode {
                border: 1px solid #d3d3d3;
                height: 300px;
            }
        </style>
        
        <h1 class="page-header"><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.title"/></h1>


        <div class="tabbable">
            <ul class="nav nav-tabs">
                <li class="active"><a href="#configuration" data-toggle="tab">Configuration</a></li>
                <li><a href="#facets" data-toggle="tab">Facets</a></li>
                <li><a href="#groovyShell" data-toggle="tab">Groovy Shell</a></li>
                <li><a href="#themes" data-toggle="tab">
                    <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.theme.available"/>
                </a></li>
                <li><a href="#layouts" data-toggle="tab">Alternative Layouts</a></li>
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
                    <h2>All your facets</h2>
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

                <%-- Groovy shell tab --%>
                <div class="tab-pane" id="groovyShell">
                    <h2><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.code"/></h2>
                    <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.title"/>
                    <ul>
                        <li><strong><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.request"/> </strong> <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.requestType"/></li>
                        <li><strong><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.woko"/></strong> <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.wokoType"/></li>
                        <li><strong><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.logs"/></strong> <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.logsType"/></li>
                    </ul>
                    
                    <div id="groovyCodeWrapper">
                        <div id="groovyCode" class="span9"></div>
                    </div>
                    <button id="btnExec" class="btn btn-primary">Execute</button>
                    <div>
                        <h2><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.log.exec"/></h2>
                        <div id="log" class="well"></div>
                    </div>
                </div>

                <%-- Layout Roller tab --%>
                <div class="tab-pane" id="layouts">
                    <ul class="thumbnails">
                        <li class="span3">
                            <div class="thumbnail">
                                <img src="http://twitter.github.com/bootstrap/assets/img/examples/bootstrap-example-marketing.png" alt="Bootstrap">
                                <h3>Default</h3>
                                <p>
                                    Provides a common fixed-width (and optionally responsive) layout.
                                </p>
                                <s:link href="/alternativeLayout?facet.sourcePage=/studio#layouts" class="btn btn-primary">
                                    Apply
                                </s:link>
                            </div>
                        </li>
                        <c:forEach items="<%=AlternativeLayout.values()%>" var="aLayout">
                            <li class="span3">
                                <div class="thumbnail">
                                    <img src="${aLayout.imgUrl}" alt="${aLayout}">
                                    <h3>${aLayout}</h3>
                                    <p>
                                        <c:out value="${aLayout.description}" escapeXml="false"/>
                                    </p>
                                    <s:link href="/alternativeLayout?facet.alternativeLayout=${aLayout}&facet.sourcePage=/studio#layouts" class="btn btn-primary">
                                        Apply
                                    </s:link>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>

                    <h3>How to use this layout permanently</h3>
                    <p>
                        Open the facet : <code>your.facets.package.MyLayout</code> and replace the the <code>getLayoutPath()</code> method with something like :
                    </p>
                    <%
                        String layoutName;
                        AlternativeLayout aLayout = (AlternativeLayout)request.getSession().getAttribute(SwitchLayout.WOKO_ALTERNATIVE_LAYOUT);
                        if (aLayout == null)
                            layoutName = "layout.jsp";
                        else
                            layoutName = "/layouts/" + aLayout.name().toLowerCase() + "-layout.jsp";
                    %>
    <pre>
    @Override
    String getLayoutPath() {
        /WEB-INF/woko/jsp/all/<%=layoutName%>"
    }
    </pre>
                </div>

                <%-- Theme Roller tab --%>
                <div class="tab-pane" id="themes">
                    <ul class="thumbnails">
                        <li class="span3">
                            <div class="thumbnail">
                                <img src="http://twitter.github.com/bootstrap/assets/img/examples/bootstrap-example-hero.jpg" width="256px" alt="Bootstrap">
                                <h3>Bootstrap</h3>
                                <s:link href="/theme?facet.sourcePage=/studio#themes" class="btn btn-primary">
                                    <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.theme.apply"/>
                                </s:link>
                            </div>
                        </li>
                        <c:forEach items="<%=Theme.values()%>" var="theme">
                            <li class="span3">
                                <div class="thumbnail">
                                    <img src="http://bootswatch.com/${fn:toLowerCase(theme)}/thumbnail.png" alt="${theme}">
                                    <h3>${theme}</h3>
                                    <s:link href="/theme?facet.theme=${theme}&facet.sourcePage=/studio#themes" class="btn btn-primary">
                                        <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.theme.apply"/>
                                    </s:link>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>

                    <jsp:include page="previewTheme.jsp"/>
                </div>
            </div>
        </div>

    </s:layout-component>
</s:layout-render>