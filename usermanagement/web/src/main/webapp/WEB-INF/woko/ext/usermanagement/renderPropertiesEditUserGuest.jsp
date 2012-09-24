<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/woko/jsp/taglibs.jsp"%>

<%@ page import="woko.facets.builtin.RenderProperties" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="woko.facets.WokoFacetContext" %>
<%@ page import="woko.Woko" %>
<%@ page import="woko.util.Util" %>
<%@ page import="woko.facets.builtin.RenderPropertyName" %>
<%@ page import="woko.persistence.ObjectStore" %>
<%@ page import="woko.facets.builtin.RenderPropertyValue" %>
<%@ page import="woko.facets.builtin.WokoFacets" %>
<%@ page import="net.sourceforge.stripes.util.CryptoUtil" %>

<%
    RenderProperties editProperties = (RenderProperties)request.getAttribute(WokoFacets.renderPropertiesEdit);
    List<String> propertyNames = editProperties.getPropertyNames();
    Map<String,Object> propertyValues = editProperties.getPropertyValues();
    WokoFacetContext<?,?,?,?> fctx = (WokoFacetContext)editProperties.getFacetContext();
    Woko<?,?,?,?> woko = fctx.getWoko();
    Object owningObject = fctx.getTargetObject();
    ObjectStore os = woko.getObjectStore();
    String mappedClassName = os.getClassMapping(owningObject.getClass());
    String formUrl = "/save/" + mappedClassName;
    String key = os.getKey(owningObject);
    if (key!=null) {
        formUrl += "/" + key;
    }
    String sourcePage = editProperties.getFragmentPath(request);
    String encryptedSourcePage = CryptoUtil.encrypt(sourcePage);
%>
<s:form action="<%=formUrl%>" class="form-horizontal">
<s:hidden name="createTransient"/>
    <s:hidden name="_sourcePage" value="<%=encryptedSourcePage%>"/>
<fieldset>

    <%-- username, email, password are hard-coded --%>
        <div class="control-group ${empty(actionBean.context.validationErrors['facet.username']) ? '' : 'error'} ">
            <s:label for="facet.username" class="control-label">
                <fmt:message bundle="${wokoBundle}" key="User.username"/>
            </s:label>
            <div class="controls">
                <div class="input-prepend">
                    <span class="add-on"><i class="icon-user"> </i></span>
                    <s:text name="facet.username" class="span4"/>
                </div>
                <s:errors field="facet.username"/>
            </div>
        </div>

        <div class="control-group ${empty(actionBean.context.validationErrors['facet.email']) ? '' : 'error'} ">
            <s:label for="facet.email" class="control-label">
                <fmt:message bundle="${wokoBundle}" key="User.email"/>
            </s:label>
            <div class="controls">
                <div class="input-prepend">
                    <span class="add-on">@</span>
                    <s:text name="facet.email" class="span4"/>
                </div>
                <s:errors field="facet.email"/>
            </div>
        </div>

        <div class="control-group ${empty(actionBean.context.validationErrors['facet.password1']) ? '' : 'error'} ">
            <s:label for="facet.password1" class="control-label">
                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password1"/>
            </s:label>
            <div class="controls">
                <div class="input-prepend">
                    <span class="add-on">
                        <i class="icon-lock"> </i>
                    </span>
                    <s:password name="facet.password1" class="span4"/>
                </div>
                <s:errors field="facet.password1"/>
            </div>
        </div>

        <div class="control-group ${empty(actionBean.context.validationErrors['facet.password2']) ? '' : 'error'} ">
            <s:label for="facet.password2" class="control-label">
                <fmt:message bundle="${wokoBundle}" key="woko.ext.usermanagement.register.password2"/>
            </s:label>
            <div class="controls">
                <div class="input-prepend">
                    <span class="add-on">
                        <i class="icon-lock"> </i>
                    </span>
                    <s:password name="facet.password2" class="span4"/>
                </div>
                <s:errors field="facet.password2"/>
            </div>
        </div>


    <%-- now display the user's props using regular object renderer --%>
<%
    for (String pName : propertyNames) {
        Object pVal = propertyValues.get(pName);

        RenderPropertyName renderPropertyName =
            woko.getFacet(WokoFacets.renderPropertyName, request, owningObject, owningObject.getClass(), true);
        renderPropertyName.setPropertyName(pName);
        String pNameFragmentPath = renderPropertyName.getFragmentPath(request);

        RenderPropertyValue editPropertyValue = Util.getRenderPropValueEditFacet(woko, request, owningObject, pName, pVal);
        String pValFragmentPath = editPropertyValue.getFragmentPath(request);

        String fullFieldName = "object." + pName;
%>
        <c:set var="fullFieldNameStripes" value="<%=fullFieldName%>"/>
        <div class="control-group ${empty(actionBean.context.validationErrors[fullFieldNameStripes]) ? '' : 'error'} ">
            <jsp:include page="<%=pNameFragmentPath%>"/>
            <div class="controls">
                <jsp:include page="<%=pValFragmentPath%>"/>
                <s:errors field="<%=fullFieldName%>"/>
            </div>
        </div>
<%
    }
%>
    <div class="form-actions">
        <s:submit name="save" class="btn btn-primary btn-large"/>
    </div>
</fieldset>
</s:form>
