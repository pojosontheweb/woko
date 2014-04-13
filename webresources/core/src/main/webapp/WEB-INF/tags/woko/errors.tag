<%@ tag import="net.sourceforge.stripes.controller.StripesConstants" %>
<%@ tag import="woko.Woko" %>
<%@ tag import="woko.facets.builtin.RenderMessages" %>
<%@ tag import="net.sourceforge.stripes.validation.ValidationErrors" %>
<%@ tag import="net.sourceforge.stripes.action.ActionBean" %>
<%@ tag import="woko.facets.builtin.RenderErrors" %>
<%@ taglib prefix="w" tagdir="/WEB-INF/tags/woko" %>
<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%
    ActionBean mainBean = (ActionBean) request.getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);
    ValidationErrors errors = mainBean.getContext().getValidationErrors();

    if (errors!=null && errors.size()>0) {
        // check if we have a renderErrors facet, and if so, use it
        Woko<?,?,?,?> woko = Woko.getWoko(application);
        RenderErrors renderErrors = woko.getFacet(RenderErrors.FACET_NAME, request, errors);
        if (renderErrors!=null) {
            // use the facet to render the messages
            String fragmentPath = renderErrors.getFragmentPath(request);
%>
            <jsp:include page="<%=fragmentPath%>"/>
<%
        } else {
            // default to stripes messages handling if no facet found
%>
            <s:errors/>
<%
        }
    }
%>