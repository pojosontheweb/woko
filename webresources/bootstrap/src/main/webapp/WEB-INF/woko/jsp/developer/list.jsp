<%@ page import="woko.persistence.ResultIterator" %>
<%@ page import="woko.facets.builtin.RenderTitle" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.ListObjects" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message var="pageTitle" key="woko.devel.list.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <%
            Woko woko = Woko.getWoko(application);
            ListObjects list = (ListObjects)request.getAttribute("list");
            String className = list.getClassName();
            ResultIterator results = list.getResults();
            int totalSize = results.getTotalSize();
            int p = list.getPage();
            int resultsPerPage = list.getResultsPerPage();
            int nbPages = totalSize / resultsPerPage;
            if (totalSize % resultsPerPage != 0) {
              nbPages++;
            }
        %>
        <h1 class="page-header">
            <fmt:message key="woko.devel.list.title">
                <fmt:param value="<%=totalSize%>"/>
                <fmt:param value="<%=className%>"/>
            </fmt:message>
        </h1>

        <c:if test="<%=nbPages>1%>">
            <div class="row-fluid">
                <s:form action="/list" class="form-inline">
                    <s:hidden name="className"/>
                    <input type="hidden"name="facet.page" value="1"/>
                    <fmt:message key="woko.devel.list.showing"/>
                    <s:select name="facet.resultsPerPage" onchange="this.form.submit()">
                        <s:option value="10">10</s:option>
                        <s:option value="25">25</s:option>
                        <s:option value="50">50</s:option>
                        <s:option value="100">100</s:option>
                        <s:option value="500">500</s:option>
                        <s:option value="1000">1000</s:option>
                    </s:select>
                    <fmt:message key="woko.devel.list.objectPerPage"/>
                </s:form>
            </div>
        </c:if>

        <ul>
            <%
              while (results.hasNext()) {
                  Object result = results.next();
                  // compute title
                  RenderTitle renderTitle = (RenderTitle)woko.getFacet("renderTitle", request, result);
                  String title = renderTitle!=null ? renderTitle.getTitle() : result.toString();
                  // compute link if view facet is available
                  String href = null;
                  String resultKey = woko.getObjectStore().getKey(result);
                  String resultClassName = woko.getObjectStore().getClassMapping(result.getClass());
                  if (woko.getFacet(WokoFacets.view, request, result)!=null) {
                      href = new StringBuilder().
                              append(request.getContextPath()).
                              append("/").
                              append(WokoFacets.view).
                              append("/").
                              append(resultClassName).
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
                      (<%=resultClassName%>)
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
                String leftMoveHref = request.getContextPath() + "/list/" + className +
                  "?facet.page=" + (p - 1);

                String rightMoveCss = p == nbPages ? "disabled" : "";
        %>
            <div class="row-fluid">
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
                            <a href="${pageContext.request.contextPath}/list/<%=className%>?facet.page=<%=i%>&facet.resultsPerPage=<%=resultsPerPage%>"><%=i%></a>
                        </li>
                    <%
                        }
                    %>

                        <li class="<%=rightMoveCss%>">
                            <a href="${pageContext.request.contextPath}/list/<%=className%>?facet.page=<%=p+1%>&facet.resultsPerPage=<%=resultsPerPage%>">»</a>
                        </li>
                    </ul>
                </div>
            </div>
        <%
            }
        %>

    </s:layout-component>
</s:layout-render>

