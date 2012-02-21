<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<w:facet facetName="<%=WokoFacets.layout%>"/>

<fmt:message var="pageTitle" key="woko.devel.cloud.pageTitle"/>
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

        <h1><fmt:message key="woko.devel.cloud.title"/></h1>

        <w:includeFacet facetName="termCloudFragment" targetObject="${termCloud.cloud}"/>
        
    </s:layout-component>
</s:layout-render>