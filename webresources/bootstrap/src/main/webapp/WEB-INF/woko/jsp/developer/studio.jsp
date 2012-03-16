<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>

<c:set var="cp" value="${pageContext.request.contextPath}"/>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<%
    Woko woko = Woko.getWoko(application);
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
                            "facet.code": $('#groovyCode').val()
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
            })

        </script>
        
        
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


                <div class="tab-pane" id="groovyShell">
                    <h2><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.title"/> </h2>
                    <ul>
                        <li><strong><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.request"/> </strong> <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.requestType"/></li>
                        <li><strong><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.woko"/></strong> <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.wokoType"/></li>
                        <li><strong><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.logs"/></strong> <fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.logsType"/></li>
                    </ul>

                    <div class="well execGroovy">
                        <fieldset>
                            <legend><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.groovy.code"/></legend>
                            <div class="control-group">
                                <textarea id="groovyCode" class="span9" placeholder="Type something…"></textarea>
                            </div>
                            <div class="control-group">
                                <button id="btnExec" class="btn btn-primary">Execute</button>
                            </div>
                        </fieldset>
                    </div>

                    <div>
                        <h2><fmt:message bundle="${wokoBundle}" key="woko.devel.studio.log.exec"/></h2>
                        <div id="log"></div>
                    </div>
                    
                    
                </div>
            </div>
        </div>

    </s:layout-component>
</s:layout-render>