<%@ page import="woko.Woko" %>
<%@ page import="net.sourceforge.jfacets.FacetDescriptor" %>
<%@ page import="woko.facets.builtin.developer.WokoStudio" %>
<%@ page import="facets.WokoStudioWithJQuery" %>
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

    <s:layout-component name="customJs">
        <script type="text/javascript" src="${pageContext.request.contextPath}/woko/js/woko.base.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/woko/js/woko.rpc.js"></script>
    </s:layout-component>

    <s:layout-component name="body">

        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>

        <script type="text/javascript">

            function replaceAll(txt, replace, with_this) {
                return txt.replace(new RegExp(replace, 'g'),with_this);
            }

            $(document).ready(function(){
                $("form.execGroovy").submit(function(e) {
                    // Get code
                    var code = $('#groovyCode').val();
                    jQuery.ajax({
                        url:"${pageContext.request.contextPath}/groovy?facet.code="+code,
                        complete: function(data){
                            var objJson = JSON.parse(data.responseText);
                            $('#log').append(replaceAll(objJson.log, '\n', '<br/>'));
                        },
                        error: function(data){
                            $('#log').append('ERROR !')
                        }
                    })

                    return false;
                });
            })

        </script>
        
        
        <h1 class="page-header"><fmt:message key="woko.devel.studio.title"/></h1>


        <div class="tabbable">
            <ul class="nav nav-tabs">
                <li class="active"><a href="#configuration" data-toggle="tab">Configuration</a></li>
                <li><a href="#facets" data-toggle="tab">Facets</a></li>
                <li><a href="#groovyShell" data-toggle="tab">Groovy Shell</a></li>
            </ul>

            <div class="tab-content">

                <%-- Configuration tab --%>
                <div class="tab-pane active" id="configuration">
                    <h2><fmt:message key="woko.devel.studio.config.title"/> </h2>
                    <ul>
                        <li><fmt:message key="woko.devel.studio.config.objectStore"/> <strong><%=woko.getObjectStore().getClass().getName()%></strong></li>
                        <li><fmt:message key="woko.devel.studio.config.userManager"/> <strong><%=woko.getUserManager().getClass().getName()%></strong></li>
                        <li><fmt:message key="woko.devel.studio.config.fallbackRoles"/> <strong><%=woko.getFallbackRoles()%></strong></li>
                        <li><fmt:message key="woko.devel.studio.config.userStrategy"/> <strong><%=woko.getUsernameResolutionStrategy()%></strong></li>
                    </ul>
                </div>


                <div class="tab-pane" id="facets">
                    <h2>All your facets</h2>
                    <table class="table table-striped table-bordered table-condensed">
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
                            WokoStudioWithJQuery studio = (WokoStudioWithJQuery)request.getAttribute("studioInJquery");
                            for (FacetDescriptor fd : studio.getFacetDescriptors()) {
                        %>
                        <tr>
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
                </div>


                <div class="tab-pane" id="groovyShell">
                    <h2><fmt:message key="woko.devel.studio.groovy.title"/> </h2>
                    <ul>
                        <li><strong><fmt:message key="woko.devel.studio.groovy.request"/> </strong> <fmt:message key="woko.devel.studio.groovy.requestType"/></li>
                        <li><strong><fmt:message key="woko.devel.studio.groovy.woko"/></strong> <fmt:message key="woko.devel.studio.groovy.wokoType"/></li>
                        <li><strong><fmt:message key="woko.devel.studio.groovy.logs"/></strong> <fmt:message key="woko.devel.studio.groovy.logsType"/></li>
                    </ul>

                    <form class="well execGroovy">
                        <fieldset>
                            <legend><fmt:message key="woko.devel.studio.groovy.code"/></legend>
                            <div class="control-group">
                                <textarea id="groovyCode" class="span9" placeholder="Type something…"></textarea>
                            </div>
                            <div class="control-group">
                                <button type="submit" class="btn btn-primary">Execute</button>
                            </div>
                        </fieldset>
                    </form>

                    <div>
                        <h2><fmt:message key="woko.devel.studio.log.exec"/></h2>
                        <div id="log"></div>
                        </code>
                    </div>
                    
                    
                </div>
            </div>
        </div>

    </s:layout-component>
</s:layout-render>