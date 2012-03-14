<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message bundle="${wokoBundle}" var="pageTitle" key="woko.devel.cloud.pageTitle"/>
<s:layout-render name="${layout.layoutPath}" layout="${layout}" pageTitle="${pageTitle}">

    <s:layout-component name="body">

        <style type="text/css">
            .cloudElem1 {
                font-size: 0.5em;
            }
            .cloudElem2 {
                font-size: 0.8em;
            }
            .cloudElem3 {
                font-size: 1.0em;
            }
            .cloudElem4 {
                font-size: 1.2em;
            }
            .cloudElem5 {
                font-size: 2.0em;
            }
        </style>

        <h1><fmt:message bundle="${wokoBundle}" key="woko.devel.cloud.title"/></h1>

        <w:includeFacet facetName="termCloudFragment" targetObject="${termCloud.cloud}"/>
        
    </s:layout-component>
</s:layout-render>