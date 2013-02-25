<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.persistence.ResultIterator" %>
<%@ page import="woko.facets.builtin.RenderTitle" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.Search" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.builtin.View" %>

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
        <h1 class="page-header">
            <fmt:message bundle="${wokoBundle}" key="woko.devel.search.title">
                <fmt:param value="<%=totalSize%>"/>
            </fmt:message>
        </h1>

        <div class="row-fluid">
            <s:form action="/search" class="form-inline" method="GET">
                <fmt:message bundle="${wokoBundle}" key="woko.devel.find.enterQuery"/>
                <s:text name="facet.query" class="input-xlarge"/>
                <s:submit name="search" class="btn btn-primary"/>
            </s:form>
        </div>


        <c:if test="<%=nbPages>1%>">
            <div class="row-fluid">
                <s:form action="/search" class="form-inline">
                    <s:hidden name="facet.query"/>
                    <s:hidden name="className"/>
                    <input type="hidden"name="facet.page" value="1"/>
                    <fmt:message bundle="${wokoBundle}" key="woko.devel.search.showing"/>
                    <s:select name="facet.resultsPerPage" onchange="this.form.submit()" class="input-small">
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
        </c:if>

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
                  if (woko.getFacet(View.FACET_NAME, request, result)!=null) {
                      href = new StringBuilder().
                              append(request.getContextPath()).
                              append("/").
                              append(View.FACET_NAME).
                              append("/").
                              append(className).
                              append("/").
                              append(resultKey).
                              toString();
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


        <%
            int nbPagesClickable = nbPages < 10 ? nbPages : 10;
            if (nbPages>1) {
                int pagerStart = p > nbPagesClickable ? p - (nbPagesClickable-1) : 1;
                String leftMoveCss = p <= 1 ? "disabled" : "";
                String leftMoveHref = leftMoveCss.equals("disabled") ? "" : request.getContextPath() + "/search?facet.query=" + query +
                        "&facet.page=" + (p - 1) + "&facet.resultsPerPage=" + resultsPerPage;

                String rightMoveCss = p == nbPages ? "disabled" : "";
                String rightMoveHref = rightMoveCss.equals("disabled") ? "" : request.getContextPath() + "/search?facet.query=" + query +
                        "&facet.page=" + (p + 1) + "&facet.resultsPerPage=" + resultsPerPage;
        %>
            <div class="pagination">
                <ul>
                    <li class="<%=leftMoveCss%>">
                        <a href="<%=leftMoveHref%>">«</a>
                    </li>
                <%
                    for (int i=pagerStart;i<=pagerStart+nbPagesClickable-1;i++) {
                        String css = "";
                        if (i==p) {
                            css = "active";
                        }

                %>
                    <li class="<%=css%>">
                        <a href="${pageContext.request.contextPath}/search?facet.query=<%=query%>&facet.page=<%=i%>&facet.resultsPerPage=<%=resultsPerPage%>"><%=i%></a>
                    </li>
                <%
                    }
                %>

                    <li class="<%=rightMoveCss%>">
                        <a href="<%=rightMoveHref%>">»</a>
                    </li>
                </ul>
            </div>
        <%
            }
        %>


    </s:layout-component>
</s:layout-render>

