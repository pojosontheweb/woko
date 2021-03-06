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
<%@ tag import="woko.util.httpcaching.WokoHttpCacheFilter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="paramName" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="paramName" alias="vn" scope="AT_END" %>
<%@ attribute name="tokenValue" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="tokenValue" alias="vv" scope="AT_END" %>
<%!
    private static final String DEFAULT_PNAME = "cacheToken";
%>
<%
    WokoHttpCacheFilter filter = WokoHttpCacheFilter.get(application);
    String cacheTokenParamName = DEFAULT_PNAME;
    String cacheTokenValue = "";
    if (filter!=null) {
        cacheTokenParamName = filter.getCacheTokenParamName();
        cacheTokenValue = filter.getCacheTokenValue();
    }
%>
<c:set var="vn"><%=cacheTokenParamName%></c:set>
<c:set var="vv"><%=cacheTokenValue%></c:set>
