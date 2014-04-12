<%@ page import="woko.facets.builtin.RenderLinks" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.facets.builtin.all.Link" %>
<%@ page import="java.util.List" %>
<%@ page import="woko.util.LinkUtil" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
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
<%
RenderLinks renderLinks = (RenderLinks)request.getAttribute(RenderLinks.FACET_NAME);
    if (renderLinks==null) {
        renderLinks = (RenderLinks)request.getAttribute(WokoFacets.renderLinksEdit);
    }
    List<Link> links = renderLinks.getLinks();
    if (links.size()>1) {
        Link first = links.get(0);
        first.setCssClass("btn btn-primary " + first.getCssClass());
        String firstLinkAttrs = LinkUtil.computeAllLinkAttributes(first, request);
%>
<div class="btn-group">
    <a<%=firstLinkAttrs%>><%=first.getText()%></a>
        <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#">
            <span class="caret"></span>
        </a>
        <ul class="dropdown-menu">
    <%
            for (int i=1; i<links.size(); i++) {
                Link l = links.get(i);
                l.setCssClass("link-" + l.getCssClass());
                String linkAttrs = LinkUtil.computeAllLinkAttributes(l, request);
    %>
            <li>
                <a<%=linkAttrs%>><%=l.getText()%></a>
            </li>
    <%      } %>
        </ul>
</div>
<%
    } else if (links.size()==1) {
        // one link only
        Link l = links.get(0);
        l.setCssClass("btn btn-primary " + l.getCssClass());
        String linkAttrs = LinkUtil.computeAllLinkAttributes(l, request);
%>
        <a<%=linkAttrs%>><%=l.getText()%></a>
<%
    }
%>