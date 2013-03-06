<%--
  ~ Copyright 2001-2012 Remi Vankeisbelck
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~       http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>
<%@ page import="woko.persistence.ResultIterator" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.ListObjects" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.RenderListItem" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.list.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <%
            Woko<?,?,?,?> woko = Woko.getWoko(application);
            ListObjects list = (ListObjects)request.getAttribute(WokoFacets.list);
            String className = list.getClassName();
            ResultIterator results = list.getResults();
            int totalSize = results.getTotalSize();
            int p = list.getPage();
            int resultsPerPage = list.getResultsPerPage();
            int nbPages = totalSize / resultsPerPage;
            if (totalSize % resultsPerPage != 0) {
              nbPages++;
            }
            String overridenH1 = list.getPageHeaderTitle();
        %>

        <c:choose>
            <c:when test="<%=overridenH1==null%>">
                <w:includeFacet facetName="<%=WokoFacets.renderListTitle%>" targetObjectClass="<%=woko.getObjectStore().getMappedClass(className)%>"/>
            </c:when>
            <c:otherwise>
                <h1 class="page-header"><%=overridenH1%></h1>
            </c:otherwise>
        </c:choose>

        <div id="wokoPaginationSettings">
            <s:form action="/list">
                <s:hidden name="className"/>
                <input type="hidden"name="facet.page" value="1"/>
                <fmt:message bundle="${wokoBundle}" key="woko.devel.list.showing"/>
                <s:select name="facet.resultsPerPage" onchange="this.form.submit()">
                    <s:option value="10">10</s:option>
                    <s:option value="25">25</s:option>
                    <s:option value="50">50</s:option>
                    <s:option value="100">100</s:option>
                    <s:option value="500">500</s:option>
                    <s:option value="1000">1000</s:option>
                </s:select>
                <fmt:message bundle="${wokoBundle}" key="woko.devel.list.objectPerPage"/>
            </s:form>
        </div>
        <%
            if (nbPages>1) {
                // Catch arguments from ResultFacet
                String args = "";
                if (! list.getArgs().isEmpty()){
                    for(Object key : list.getArgs().keySet()){
                        args += "&" + key + "=" + list.getArgs().get(key);
                    }
                }
        %>
            <div class="wokoPagination">
                <%
                    for (int i=1;i<=nbPages;i++) {
                        if (i==p) {
                %>
                    <span class="wokoCurrentPage"><%=i%></span>

                <%      } else {
                            String nextHref = request.getContextPath() + "/list/" + className +
                                    "?facet.page=" + i  + "&facet.resultsPerPage=" + resultsPerPage + args;
                %>
                    <span><a href="<%=nextHref%>"><%=i%></a></span>
                <%
                        }
                        if (i<nbPages) {
                %>
                |
                <%
                        }
                    }
                %>
            </div>
        <%
            }
        %>
        <ul class="<%=list.getListWrapperCssClass()%>">
            <%
                while (results.hasNext()) {
                    Object result = results.next();
                    RenderListItem renderListItem = woko.getFacet(WokoFacets.renderListItem, request, result, result.getClass(),true );
                    String fragmentPath = renderListItem.getFragmentPath(request);

            %>
            <li class="<%=renderListItem.getItemWrapperCssClass()%>">
                <jsp:include page="<%=fragmentPath%>"/>
            </li>
            <%
                }
            %>
        </ul>

        

    </s:layout-component>
</s:layout-render>

