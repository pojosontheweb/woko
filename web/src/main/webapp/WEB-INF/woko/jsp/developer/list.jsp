<%@ page import="woko2.facets.builtin.developer.List" %>
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
            List list = (List)request.getAttribute("list");
            String className = list.getClassName();
            ResultIterator results = list.getResults();
            int totalSize = results.getTotalSize();
            int start = results.getStart();
            int limit = results.getLimit();
        %>
        <h1><c:out value="<%=totalSize%>"/> object(s) found for class <c:out value="<%=className%>"/></h1>
        <p>Displaying <c:out value="<%=start%>"/> to <c:out value="<%=start + limit - 1%>"/> :</p>
        <ul>
            <%
              while (results.hasNext()) {
                  Object result = results.next();
                  // compute title
                  RenderTitle renderTitle = (RenderTitle)woko.getFacet(RenderTitle.name, request, result);
                  String title = renderTitle!=null ? renderTitle.getTitle() : result.toString();
                  // compute link if view facet is available
                  String href = null;
                  if (woko.getFacet("view", request, result)!=null) {
                      String resultKey = woko.getObjectStore().getKey(result);
                      href = request.getContextPath() + "/view/" + className + "/" + resultKey;
                  }
            %>
                  <li>
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
                  </li>
            <%
              }
            %>
        </ul>
        

    </s:layout-component>
</s:layout-render>

