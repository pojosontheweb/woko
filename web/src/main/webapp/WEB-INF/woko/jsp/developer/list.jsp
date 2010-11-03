<%@ page import="woko2.facets.builtin.developer.ListObjects" %>
<%@ page import="woko2.persistence.ResultIterator" %>
<%@ page import="woko2.facets.builtin.RenderTitle" %>
<%@ page import="woko2.Woko" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<w:facet facetName="layout"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="Object list">
    <s:layout-component name="sidebarLinks">
        <ul class="menu">
            <li><a href="#">Help</a></li>
        </ul>

    </s:layout-component>
    <s:layout-component name="body">
        <%
            Woko woko = Woko.getWoko(application);
            ListObjects list = (ListObjects)request.getAttribute("list");
            String className = list.getClassName();
            ResultIterator results = list.getResults();
            int totalSize = results.getTotalSize();
            int p = list.getP();
            int resultsPerPage = list.getResultsPerPage();
            int nbPages = totalSize / resultsPerPage + 1;
        %>
        <h1><c:out value="<%=totalSize%>"/> object(s) found for class <c:out value="<%=className%>"/></h1>
        <div id="wokoPaginationSettings">
            <s:form action="/list">
                <s:hidden name="className"/>
                <input type="hidden"name="facet.p" value="1"/>
                Showing
                <s:select name="facet.resultsPerPage" onchange="this.form.submit()">
                    <s:option value="10">10</s:option>
                    <s:option value="25">25</s:option>
                    <s:option value="50">50</s:option>
                    <s:option value="100">100</s:option>
                    <s:option value="500">500</s:option>
                    <s:option value="1000">1000</s:option>
                </s:select>
                objects / page
            </s:form>
        </div>
        <%
            if (nbPages>1) {
        %>
            <div class="wokoPagination">
                <%
                    for (int i=1;i<nbPages+1;i++) {
                        if (i==p) {
                %>
                    <span class="wokoCurrentPage"><%=i%></span>

                <%      } else { %>
                    <span><a href="${pageContext.request.contextPath}/list/<%=className%>?facet.p=<%=i%>&facet.resultsPerPage=<%=resultsPerPage%>"><%=i%></a></span>
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
                  RenderTitle renderTitle = (RenderTitle)woko.getFacet(RenderTitle.name, request, result);
                  String title = renderTitle!=null ? renderTitle.getTitle() : result.toString();
                  // compute link if view facet is available
                  String href = null;
                  String resultKey = woko.getObjectStore().getKey(result);
                  if (woko.getFacet("view", request, result)!=null) {
                      href = request.getContextPath() + "/view/" + className + "/" + resultKey;
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

