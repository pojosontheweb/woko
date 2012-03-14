<%@ tag import="woko.Woko" %>
<%@ tag import="woko.util.LinkUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="object" required="false" type="java.lang.Object" %>
<%@ attribute name="targetObjectClass" required="false" type="java.lang.Class" %>
<%@ attribute name="facetName" required="false" type="java.lang.String" %>
<%@ attribute name="var" required="true" type="java.lang.String" rtexprvalue="false" %>
<%@ variable name-from-attribute="var" alias="daLink" scope="AT_END" %>
<%--
  ~ Copyright 2001-2010 Remi Vankeisbelck
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

<%
    String url;
    if (object!=null) {
        url = LinkUtil.getUrl(Woko.getWoko(application), object, facetName);
    } else if (targetObjectClass!=null) {
        url = facetName + "/" + Woko.getWoko(application).getObjectStore().getClassMapping(targetObjectClass);
    } else {
        throw new IllegalStateException("neither object nor targetObjectClass provided !");
    }
    String path = request.getContextPath();
%>
<c:set var="daLink"><%=path + "/" + url%></c:set>
