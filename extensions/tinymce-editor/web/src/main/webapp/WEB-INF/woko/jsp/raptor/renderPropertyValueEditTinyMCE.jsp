<%--
  ~ Copyright 2001-2013 Remi Vankeisbelck
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
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.ext.raptor.RenderPropertyValueEditTinyMCE" %>
<%@ page import="java.util.UUID" %>
<%
    RenderPropertyValueEditTinyMCE<?,?,?,?> renderPropertyValue =
            (RenderPropertyValueEditTinyMCE<?,?,?,?>)request.getAttribute(WokoFacets.renderPropertyValueEdit);
    WokoFacetContext<?,?,?,?> fctx = renderPropertyValue.getFacetContext();
    ObjectStore os = fctx.getWoko().getObjectStore();
    String propertyName = renderPropertyValue.getPropertyName();
    Object owningObject = renderPropertyValue.getOwningObject();
    String propertyClassName = os.getClassMapping(Util.getPropertyType(owningObject.getClass(), propertyName));
    String fullFieldName = renderPropertyValue.getFieldPrefix() + "." + propertyName;
    String textAreaId = UUID.randomUUID().toString();
%>
<span class="wokoPropertyValueEdit">
    <span class="<%=propertyName%> <%=propertyClassName%>">
        <s:textarea id="<%=textAreaId%>" name="<%=fullFieldName%>"/>
    </span>
</span>
<w:cacheToken paramName="cacheToken" tokenValue="cacheTokenValue"/>
<c:set var="cacheTokenParams" value="${cacheToken}=${cacheTokenValue}"/>
<script type="text/javascript" src="${cp}/tiny_mce/tiny_mce.js?${cacheTokenParams}"></script>
<script type="text/javascript">
    $(function() {

        tinyMCE.init({
                // General options
                mode : "exact",
                elements: "<%=textAreaId%>",
                theme : "advanced",
                plugins : "autolink,lists,spellchecker,pagebreak,style,layer,table,save,advhr,advimage,advlink,emotions,iespell,inlinepopups,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,fullscreen,noneditable,visualchars,nonbreaking,xhtmlxtras,template",

                // Theme options
                theme_advanced_buttons1 : "save,newdocument,|,bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect,fontselect,fontsizeselect",
                theme_advanced_buttons2 : "cut,copy,paste,pastetext,pasteword,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,undo,redo,|,link,unlink,anchor,image,cleanup,help,code,|,insertdate,inserttime,preview,|,forecolor,backcolor",
                theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,sub,sup,|,charmap,emotions,iespell,media,advhr,|,print,|,ltr,rtl,|,fullscreen",
                theme_advanced_buttons4 : "insertlayer,moveforward,movebackward,absolute,|,styleprops,spellchecker,|,cite,abbr,acronym,del,ins,attribs,|,visualchars,nonbreaking,template,blockquote,pagebreak,|,insertfile,insertimage",
                theme_advanced_toolbar_location : "top",
                theme_advanced_toolbar_align : "left",
                theme_advanced_statusbar_location : "bottom",
                theme_advanced_resizing : true,

                // Skin options
                skin : "o2k7",
                skin_variant : "silver",

                // Example content CSS (should be your site CSS)
//                content_css : "css/example.css",

                // Drop lists for link/image/media/template dialogs
//                template_external_list_url : "js/template_list.js",
//                external_link_list_url : "js/link_list.js",
//                external_image_list_url : "js/image_list.js",
//                media_external_list_url : "js/media_list.js",
//
//                // Replace values for the template plugin
//                template_replace_values : {
//                        username : "Some User",
//                        staffid : "991234"
//                }
        });

    });
</script>


