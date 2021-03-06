<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.persistence.ResultIterator" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.facets.builtin.*" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko.persistence.ObjectStore" %>

<w:facet facetName="<%=Layout.FACET_NAME%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.list.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <%
            Woko<?,?,?,?> woko = Woko.getWoko(application);
            ListObjects list = (ListObjects)request.getAttribute(ListObjects.FACET_NAME);
            String className = list.getClassName();
            ResultIterator results = list.getResults();
            int totalSize = results.getTotalSize();
            int p = list.getPage();
            int resultsPerPage = list.getResultsPerPage();
            int nbPages = totalSize / resultsPerPage;
            if (totalSize % resultsPerPage != 0) {
              nbPages++;
            }
            String listWrapperClass = list.getListWrapperCssClass();
            if (listWrapperClass==null) {
                listWrapperClass = "list " + className;
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

        <c:if test="<%=nbPages>1%>">
            <div class="row-fluid">
                <s:form action="/list" class="form-inline">
                    <s:hidden name="className"/>
                    <input type="hidden"name="facet.page" value="1"/>
                    <fmt:message bundle="${wokoBundle}" key="woko.devel.list.showing"/>
                    <s:select name="facet.resultsPerPage" onchange="this.form.submit()" class="input-small">
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
        </c:if>
            <ul class="<%=listWrapperClass%>">
                <%
                  ObjectStore objectStore = woko.getObjectStore();
                  while (results.hasNext()) {
                      Object result = results.next();
                      RenderListItem renderListItem = woko.getFacet(
                              RenderListItem.FACET_NAME, request, result, result.getClass(),true );
                      String fragmentPath = renderListItem.getFragmentPath(request);
                      String listItemClass = renderListItem.getItemWrapperCssClass();
                      if (listItemClass==null) {
                          listItemClass = "item " + objectStore.getClassMapping(objectStore.getObjectClass(result));
                      }
                %>
                        <li class="<%=listItemClass%>">
                            <jsp:include page="<%=fragmentPath%>"/>
                        </li>
                <%
                  }
                %>
            </ul>

        <%
            int nbPagesClickable = nbPages < 10 ? nbPages : 10;
            if (nbPages>1) {
                int pagerStart = p > nbPagesClickable ? p - (nbPagesClickable-1) : 1;

                // Catch arguments from ResultFacet
                String args = "";
                if (list.getArgs() != null)  {
                    for(Object key : list.getArgs().keySet()){
                        args += "&" + key + "=" + list.getArgs().get(key);
                    }
                }

                String leftMoveCss = p <= 1 ? "disabled" : "";
                String leftMoveHref = leftMoveCss.equals("disabled") ? "" : request.getContextPath() + "/list/" + className +
                  "?facet.page=" + (p - 1) + "&facet.resultsPerPage=" + resultsPerPage + args;

                String rightMoveCss = p == nbPages ? "disabled" : "";
                String rightMoveHref = rightMoveCss.equals("disabled") ? "" : request.getContextPath() + "/list/" + className +
                        "?facet.page=" + (p + 1) + "&facet.resultsPerPage=" + resultsPerPage + args;
        %>

            <div class="pagination">
                <ul>
                    <li class="<%=leftMoveCss%>">
                        <a href="<%=leftMoveHref%>">«</a>
                    </li>
                <%
                    for (int i=pagerStart;i<=pagerStart+nbPagesClickable-1;i++) {
                        String css = "";
                        String currentPageHref = request.getContextPath() + "/list/" + className +
                                "?facet.page=" + i  + "&facet.resultsPerPage=" + resultsPerPage + args;

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
            </div>

        <%
            }
        %>

    </s:layout-component>
</s:layout-render>

