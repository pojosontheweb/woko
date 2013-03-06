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
<%@ page import="woko.facets.builtin.Search" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.util.Util" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>
<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.search.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <%
            Woko<?,?,?,?> woko = Woko.getWoko(application);
            Search search = (Search)request.getAttribute(WokoFacets.search);
            ResultIterator results = search.getResults();
            String query = search.getQuery();
            int totalSize = results.getTotalSize();
            int p = search.getPage();
            int resultsPerPage = search.getResultsPerPage();
            int nbPages = totalSize / resultsPerPage;
            if (totalSize % resultsPerPage != 0) {
              nbPages++;
            }
        %>
        <h1>
            <fmt:message bundle="${wokoBundle}" key="woko.devel.search.title">
                <fmt:param value="<%=totalSize%>"/>
            </fmt:message>
        </h1>
        <div class="wokoSearchForm">
            <s:form action="/search" method="GET">
                <s:text name="facet.query" size="30"/>
                <s:submit name="search"/>
            </s:form>            
        </div>
        <%
            if (nbPages>1) {
                // Catch arguments from ResultFacet
                String args = "";
                if (! search.getArgs().isEmpty()){
                    for(Object key : search.getArgs().keySet()){
                        args += "&" + key + "=" + search.getArgs().get(key);
                    }
                }
        %>
            <div id="wokoPaginationSettings">
                <s:form action="/search">
                    <s:hidden name="facet.query"/>
                    <s:hidden name="className"/>
                    <input type="hidden"name="facet.page" value="1"/>
                    <fmt:message bundle="${wokoBundle}" key="woko.devel.search.showing"/>
                    <s:select name="facet.resultsPerPage" onchange="this.form.submit()">
                        <s:option value="10">10</s:option>
                        <s:option value="25">25</s:option>
                        <s:option value="50">50</s:option>
                        <s:option value="100">100</s:option>
                        <s:option value="500">500</s:option>
                        <s:option value="1000">1000</s:option>
                    </s:select>
                    <fmt:message bundle="${wokoBundle}" key="woko.devel.search.objectPerPage"/>
                </s:form>
            </div>
            <div class="wokoPagination">
                <%
                    for (int i=1;i<=nbPages;i++) {
                        if (i==p) {
                %>
                    <span class="wokoCurrentPage"><%=i%></span>

                <%      } else {
                            String currentPageHref = request.getContextPath() + "/search?facet.query=" + query +
                                    "&facet.page=" + (p - 1) + "&facet.resultsPerPage=" + resultsPerPage + args;
                %>
                    <span><a href="<%=currentPageHref%>"><%=i%></a></span>
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
        <ul>
            <%
              while (results.hasNext()) {
                  Object result = results.next();
                  // compute title
                  String title = Util.getTitle(request, result);
                  // compute link if view facet is available
                  String href = null;
                  String resultKey = woko.getObjectStore().getKey(result);
                  String className = woko.getObjectStore().getClassMapping(result.getClass());
                  if (woko.getFacet(WokoFacets.view, request, result)!=null) {
                      href = request.getContextPath() + "/" + WokoFacets.view + "/" + className + "/" + resultKey;
                  }
            %>
                  <li>
                      <%=resultKey%> - 
            <%
                  if (href!=null) {
            %>
                    <a href="<%=href%>">
            <%
                  }
            %>
                    <c:out value="<%=title%>"/>
            <%
                  if (href!=null) {
            %>
                    </a>
            <%
                  }
            %>
                      (<%=className%>)
                  </li>
            <%
              }
            %>
        </ul>

        

    </s:layout-component>
</s:layout-render>

