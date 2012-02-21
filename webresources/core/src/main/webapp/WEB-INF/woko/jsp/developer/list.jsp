<%@ page import="woko.persistence.ResultIterator" %>
<%@ page import="woko.facets.builtin.RenderTitle" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.ListObjects" %>
<%@ page import="woko.util.LinkUtil" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="layout"/>

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
        <h1>
            <fmt:message key="woko.devel.list.title">
                <fmt:param value="<%=totalSize%>"/>
                <fmt:param value="<%=className%>"/>
            </fmt:message>
        </h1>
        <div id="wokoPaginationSettings">
            <s:form action="/list">
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
        <%
            if (nbPages>1) {
        %>
            <div class="wokoPagination">
                <%
                    for (int i=1;i<=nbPages;i++) {
                        if (i==p) {
                %>
                    <span class="wokoCurrentPage"><%=i%></span>

                <%      } else { %>
                    <span><a href="${pageContext.request.contextPath}/list/<%=className%>?facet.page=<%=i%>&facet.resultsPerPage=<%=resultsPerPage%>"><%=i%></a></span>
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
                  RenderTitle renderTitle = (RenderTitle)woko.getFacet("renderTitle", request, result);
                  String title = renderTitle!=null ? renderTitle.getTitle() : result.toString();
                  // compute link if view facet is available
                  String href = null;
                  String resultKey = woko.getObjectStore().getKey(result);
                  String resultClassName = woko.getObjectStore().getClassMapping(result.getClass());
                  if (woko.getFacet(WokoFacets.view, request, result)!=null) {
                      href = request.getContextPath() + "/" + WokoFacets.view + "/" + resultClassName + "/" + resultKey;
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

        

    </s:layout-component>
</s:layout-render>

