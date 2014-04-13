<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.persistence.ResultIterator" %>
<%@ page import="woko.facets.builtin.RenderTitle" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.Search" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.builtin.View" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.util.LinkUtil" %>

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

        <div class="container">

            <h1 class="page-header">
                <fmt:message bundle="${wokoBundle}" key="woko.devel.search.title">
                    <fmt:param value="<%=totalSize%>"/>
                </fmt:message>
            </h1>

            <div class="row">
                <div class="col-lg-8 col-sm-8">
                    <w:b3-search-form/>
                </div>
                <div class="col-lg-4 col-sm-4 hidden-xs">
                    <c:if test="<%= nbPages > 1 %>">
                        <s:form action="/search" class="form-inline" method="GET">
                            <s:hidden name="facet.query"/>
                            <s:hidden name="className"/>
                            <input type="hidden" name="facet.page" value="1"/>
                            <s:select name="facet.resultsPerPage" onchange="this.form.submit()" class="form-control">
                                <s:option value="10">10</s:option>
                                <s:option value="25">25</s:option>
                                <s:option value="50">50</s:option>
                                <s:option value="100">100</s:option>
                                <s:option value="500">500</s:option>
                                <s:option value="1000">1000</s:option>
                            </s:select>
                            <fmt:message bundle="${wokoBundle}" key="woko.devel.search.objectPerPage"/>
                        </s:form>
                    </c:if>
                </div>
            </div>

            <hr/>

            <%
              ObjectStore objectStore = woko.getObjectStore();
              while (results.hasNext()) {
                  Object result = results.next();
                  // compute title
                  String title = Util.getTitle(request, result);
                  // compute link if view facet is available
                  String href = null;
                  String resultKey = objectStore.getKey(result);
                  String className = objectStore.getClassMapping(objectStore.getObjectClass(result));
                  if (woko.getFacet(View.FACET_NAME, request, result)!=null) {
                      href = request.getContextPath() + "/" + LinkUtil.getUrl(woko, result, View.FACET_NAME);
                  }
            %>
                  <div class="row">
                      <div class="col-lg-12">
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
                  </div>
                      </div>
            <%
              }
            %>

            <hr/>

            <%
                int nbPagesClickable = nbPages < 10 ? nbPages : 10;
                if (nbPages>1) {
                    int pagerStart = p > nbPagesClickable ? p - (nbPagesClickable-1) : 1;

                    // Catch arguments from ResultFacet
                    String args = "";
                    if (search.getArgs() != null) {
                        for(Object key : search.getArgs().keySet()){
                            args += "&" + key + "=" + search.getArgs().get(key);
                        }
                    }

                    String leftMoveCss = p <= 1 ? "disabled" : "";
                    String leftMoveHref = leftMoveCss.equals("disabled") ? "" : request.getContextPath() + "/search?facet.query=" + query +
                            "&facet.page=" + (p - 1) + "&facet.resultsPerPage=" + resultsPerPage + args;

                    String rightMoveCss = p == nbPages ? "disabled" : "";
                    String rightMoveHref = rightMoveCss.equals("disabled") ? "" : request.getContextPath() + "/search?facet.query=" + query +
                            "&facet.page=" + (p + 1) + "&facet.resultsPerPage=" + resultsPerPage + args;
            %>
                    <ul class="pagination">
                        <li class="<%=leftMoveCss%>">
                            <a href="<%=leftMoveHref%>">«</a>
                        </li>
                    <%
                        for (int i=pagerStart;i<=pagerStart+nbPagesClickable-1;i++) {
                            String css = "";
                            String currentPageHref = request.getContextPath() + "/search?facet.query=" + query +
                                    "&facet.page=" + i + "&facet.resultsPerPage=" + resultsPerPage + args;

                            if (i==p) {
                                css = "active";
                            }

                    %>
                        <li class="<%=css%>">
                            <a href="<%=currentPageHref%>"><%=i%></a>
                        </li>
                    <%
                        }
                    %>

                        <li class="<%=rightMoveCss%>">
                            <a href="<%=rightMoveHref%>">»</a>
                        </li>
                    </ul>
            <%
                }
            %>

        </div>

    </s:layout-component>
</s:layout-render>

