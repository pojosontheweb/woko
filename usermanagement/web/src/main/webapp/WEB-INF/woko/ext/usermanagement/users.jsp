<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.persistence.ResultIterator" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.builtin.*" %>
<%@ page import="woko.facets.builtin.all.Link" %>
<%@ page import="woko.util.LinkUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="woko.ext.usermanagement.facets.usermanager.Users" %>
<%@ page import="woko.ext.usermanagement.hibernate.HibernateUserManager" %>
<%@ page import="woko.ext.usermanagement.facets.usermanager.ListUsers" %>
<%@ page import="woko.ext.usermanagement.core.User" %>
<%@ page import="woko.ext.usermanagement.core.AccountStatus" %>

<w:facet facetName="<%=Layout.FACET_NAME%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.ext.usermanagement.users.page.title"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">
    <s:layout-component name="body">
        <%
            Woko woko = Woko.getWoko(application);
            Users users = (Users)request.getAttribute("users");
            HibernateUserManager bdUm = (HibernateUserManager)woko.getUserManager();
            String className = woko.getObjectStore().getClassMapping(bdUm.getUserClass());
            ResultIterator results = users.getResults();
            int totalSize = results.getTotalSize();
            int p = users.getPage();
            int resultsPerPage = users.getResultsPerPage();
            int nbPages = totalSize / resultsPerPage;
            if (totalSize % resultsPerPage != 0) {
              nbPages++;
            }
            String listWrapperClass = users.getListWrapperCssClass();
            if (listWrapperClass==null) {
                listWrapperClass = "table " + className;
            }
            String overridenH1 = users.getPageHeaderTitle();
        %>
        <h1 class="page-header">
            <c:choose>
                <c:when test="<%=overridenH1==null%>">
                    <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.users.page.header">
                        <fmt:param value="<%=totalSize%>"/>
                        <fmt:param value="<%=className%>"/>
                    </fmt:message>
                </c:when>
                <c:otherwise>
                    <%=overridenH1%>
                </c:otherwise>
            </c:choose>
        </h1>

        <p>
            <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.users.top.text"/>
            <s:link href="/save" class="btn btn-primary pull-right" addSourcePage="true">
                <s:param name="className" value="<%=className%>"/>
                <s:param name="createTransient" value="true"/>
                <s:param name="facet.roles" value="all"/>
                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.users.create.user"/>
            </s:link>
        </p>

        <h2></h2>

        <c:if test="<%=nbPages>1%>">
            <div class="row-fluid">
                <s:form action="/users" class="form-inline">
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
        </c:if>

            <%
                List<Map<String,Object>> propertyValues = new ArrayList<Map<String, Object>>();
                List<String> rowCssClasses = new ArrayList<String>();
                List<Object> resultsList = new ArrayList<Object>();
                List<String> propertyNames = users.getPropertyNames();

                while (results.hasNext()) {
                    Object result = results.next();
                    resultsList.add(result);
                    RenderProperties rpResult = (RenderProperties)woko.getFacet(
                            RenderProperties.FACET_NAME, request, result, result.getClass());
                    Map<String,Object> resultPropValues = rpResult.getPropertyValues();
                    propertyValues.add(resultPropValues);
                    RenderListItem renderListItem = (RenderListItem)woko.getFacet(WokoFacets.renderListItem, request, result, result.getClass(),true );
                    String liWrapperClass = renderListItem.getItemWrapperCssClass();
                    if (liWrapperClass==null) {
                        liWrapperClass = "";
                    }
                    rowCssClasses.add(liWrapperClass);
                }
            %>
            <table class="<%=listWrapperClass%>">
                <thead>
                <tr>
                    <%
                        for (String propName : propertyNames) {
                            String labelMsgKey = "User." + propName;
                            String msg = woko.getLocalizedMessage(request, labelMsgKey);
                    %>
                    <th>
                        <c:out value="<%=msg%>"/>
                    </th>
                    <%
                        }
                    %>
                    <th><fmt:message bundle="${wokoBundle}" key="woko.devel.list.actions.column.label"/></th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (int i=0; i<resultsList.size(); i++) {
                        User result = (User)resultsList.get(i);
                        String liWrapperClass = rowCssClasses.get(i);
                        if (liWrapperClass==null || liWrapperClass.equals("")) {
                            AccountStatus s = result.getAccountStatus();
                            if (s.equals(AccountStatus.Registered)) {
                                liWrapperClass = "warning";
                            } else if (s.equals(AccountStatus.Blocked)) {
                                liWrapperClass = "error";
                            }
                        }
                %>
                        <tr class="<%=liWrapperClass%>">
                            <%
                                for (String propName : propertyNames) {
                                    Object propVal = propertyValues.get(i).get(propName);
                                    RenderPropertyValue rpv = Util.getRenderPropValueFacet(
                                            woko, request, result, propName, propVal);
                                    String pValFragmentPath = rpv.getFragmentPath(request);
                            %>
                            <td>
                                <jsp:include page="<%=pValFragmentPath%>"/>
                            </td>
                            <%
                                }
                            %>
                            <td>
                                <div class="btn-group">
                                <%
                                    View view = (View)woko.getFacet(View.FACET_NAME, request, result);
                                    if (view!=null) {
                                        String href = request.getContextPath() + "/" + LinkUtil.getUrl(Woko.getWoko(application), result, "view");
                                %>
                                    <a href="<%=href%>" class="btn view">
                                        <fmt:message bundle="${wokoBundle}" key="woko.links.view"/>
                                    </a>
                                <%
                                    }
                                    // Grab available links !
                                    RenderLinks rl = (RenderLinks)woko.getFacet(RenderLinks.FACET_NAME, request, result);
                                    for (Link l : rl.getLinks()) {
                                        String href = request.getContextPath() + "/" + l.getHref();
                                        String cssClass = l.getCssClass();
                                        String text = l.getText();
                                %>
                                    <a href="<%=href%>" class="btn <%=cssClass%>"><c:out value="<%=text%>"/></a>
                                <%
                                    }
                                %>
                                </div>
                            </td>

                        </tr>
                <%
                  }
                %>
                </tbody>
            </table>

        <%
            int nbPagesClickable = nbPages < 10 ? nbPages : 10;
            if (nbPages>1) {
                int pagerStart = p > nbPagesClickable ? p - (nbPagesClickable-1) : 1;
                String leftMoveCss = p <= 1 ? "disabled" : "";
                String leftMoveHref = request.getContextPath() + "/users/" + className +
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
                            <a href="${pageContext.request.contextPath}/users/<%=className%>?facet.page=<%=i%>&facet.resultsPerPage=<%=resultsPerPage%>"><%=i%></a>
                        </li>
                    <%
                        }
                    %>

                        <li class="<%=rightMoveCss%>">
                            <a href="${pageContext.request.contextPath}/users/<%=className%>?facet.page=<%=p+1%>&facet.resultsPerPage=<%=resultsPerPage%>">»</a>
                        </li>
                    </ul>
                </div>
            </div>
        <%
            }
        %>

    </s:layout-component>
</s:layout-render>

