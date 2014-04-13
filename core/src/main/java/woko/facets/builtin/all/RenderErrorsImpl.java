package woko.facets.builtin.all;

import net.sourceforge.jfacets.IFacetDescriptorManager;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.validation.ValidationError;
import woko.facets.BaseFragmentFacet;
import woko.facets.builtin.RenderErrors;
import woko.persistence.ObjectStore;
import woko.users.UserManager;
import woko.users.UsernameResolutionStrategy;

import java.util.List;

@FacetKey(name= RenderErrors.FACET_NAME, profileId = "all", targetObjectType = List.class)
public class RenderErrorsImpl<
        OsType extends ObjectStore,
        UmType extends UserManager,
        UnsType extends UsernameResolutionStrategy,
        FdmType extends IFacetDescriptorManager> extends BaseFragmentFacet<OsType, UmType, UnsType, FdmType> implements RenderErrors {

    private static final String JSP_PATH = "/WEB-INF/woko/jsp/all/renderErrors.jsp";

    @Override
    public String getPath() {
        return JSP_PATH;
    }

    @SuppressWarnings("unchecked")
    public List<ValidationError> getErrors() {
        return (List<ValidationError>)getFacetContext().getTargetObject();
    }

    @Override
    public boolean isEscapeXml() {
        return true;
    }
}
